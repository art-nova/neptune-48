package game;

import data.DataManager;
import data.LevelData;
import data.LevelIdentifier;
import data.PlayerData;
import game.abilities.AbilityManager;
import game.events.*;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Entity;
import game.gameobjects.Tile;
import game.gameobjects.particles.ParticleManager;
import game.obstacles.ObstacleManager;
import game.utils.GamePanelGraphics;
import misc.AudioManager;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Implementation of the panel in which the core game loop (2048 gameplay, attacking, triggering abilities etc.) happens.
 *
 * @author Artem Novak
 */
public class GamePanel extends JPanel implements Runnable {
    // How many logical updates and visual repaints are done per second
    public static final int TPS = 60;
    // Game state
    public static final int PLAYING = 0, PAUSED = 1, ENDING = 2, ENDED = 3;
    // Game mode
    public static final int GAME_MODE_ATTACK = 0, GAME_MODE_REPAIR = 1;

    private final LevelData levelData;
    private final PlayerData playerData;
    private final GamePanelGraphics graphics;
    private final ActionHandler actionHandler = new ActionHandler();
    private final Thread gameThread = new Thread(this);
    private final Countdown countdown;
    private final Board board;
    private final Entity entity;
    private final ObstacleManager obstacleManager;
    private final AbilityManager abilityManager;
    private final ParticleManager particleManager;
    private final AudioManager audioManager;
    private final List<GameOverListener> gameOverListeners = new ArrayList<>();
    private final List<StateListener> stateListeners = new ArrayList<>();
    private final List<UIDataListener> uiDataListeners = new ArrayList<>();
    private final int baseTileDamage;
    private final int gameMode;

    private int state = PLAYING;

    /**
     * Constructs a game panel with given graphics manager object.
     *
     * @param levelData {@link LevelData} object storing information about this panel's level
     * @param playerData {@link PlayerData} object storing information about current player profile
     * @param baseFrame base {@link JFrame} of this panel
     * @param audioManager {@link AudioManager} supplied from outside (is used for playing background music and SFX)
     */
    public GamePanel(LevelData levelData, PlayerData playerData, JFrame baseFrame, AudioManager audioManager) throws IOException {
        this.levelData = levelData;
        this.playerData = playerData;
        this.audioManager = audioManager;
        this.baseTileDamage = levelData.getBaseTileDamage();
        this.graphics = levelData.generateGraphics();
        this.gameMode = levelData.getGameMode();
        this.particleManager = new ParticleManager(this);
        this.entity = new Entity(0, 0, levelData.getEntityHealth(), levelData.getEntityTolerance(), this);
        this.board = new Board(0, graphics.getEntityHeight() + graphics.getEntityBoardDistance(), levelData.getBoardSize(), levelData.getBoardSize(), 1, this);
        this.countdown = new Countdown(levelData.getTurns(), this);
        this.obstacleManager = new ObstacleManager(levelData.getObstacleWeights(), levelData.getMinObstacleInterval(), levelData.getMaxObstacleInterval(), this);
        this.abilityManager = new AbilityManager(playerData.getActiveAbility1(), playerData.getActiveAbility2(), playerData.getPassiveAbility(), this);
        baseFrame.addKeyListener(new KeyHandler(this));
        this.addMouseListener(new MouseHandler(this));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(graphics.getEntityWidth(), board.getPreferredHeight() + graphics.getEntityBoardDistance() + graphics.getEntityHeight()));
        board.setX((graphics.getEntityWidth() - board.getPreferredWidth())/2f);

        // Tracking level 11 tiles and starting an attack with one as soon as it appears.
        board.addTurnListener(() -> {
            List<BoardCell> cells = board.getCellsByPredicate(x -> {
                Tile tile = board.getTileInCell(x);
                return tile != null && tile.getLevel() == 11;
            });
            if (!cells.isEmpty()) {
                board.addStateListener(new StateListener() {
                    @Override
                    public void onStateChanged(int oldState, int newState) {
                        if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                            // When there are actually no other scheduled animations left (if there are, they set board's state to ANIMATING).
                            if (board.getState() == Board.IDLE) {
                                board.removeStateListener(this);
                                abilityManager.getAttack().startAttack(cells.get(0));
                            }
                        }
                    }
                });
            }
        });

        board.generateRandomTile();
        board.generateRandomTile();
        audioManager.setBG("level"+levelData.getLevelIdentifier().index());
        audioManager.playBG();
        gameThread.start();
    }

    @Override
    public void run() {
        double frameInterval = 1000000000/TPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        countdown.addUIDataListener(() -> {if (countdown.getTurns() <= 0) loseLevel();});
        countdown.start();

        while (state != ENDED) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/frameInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                if (state == PLAYING || state == ENDING) {
                    for (int i = 0; i < (int)delta; i++) {
                        update();
                    }
                    repaint();
                    delta -= (int)delta;

                    if (state == ENDING && board.getState() == Board.IDLE && entity.getState() == Entity.IDLE && particleManager.getState() == ParticleManager.IDLE) setState(ENDED);
                }

                if ((state == PLAYING) && actionHandler.isPriorityAction("pause")) {
                    setState(PAUSED);
                    actionHandler.clearAction("pause");
                }
                else if (state == PAUSED && actionHandler.isPriorityAction("unpause")) {
                    setState(PLAYING);
                    actionHandler.clearAction("unpause");
                }
            }
        }
    }

    /**
     * Updates all components of the panel.
     */
    public void update() {
        if (state != ENDING) {
            abilityManager.update();
        }
        entity.update();
        board.update();
        particleManager.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        entity.render(g2d);
        board.render(g2d);
        particleManager.render(g2d);
        g.dispose();
    }

    /**
     * Finishes the game without any changes to {@link PlayerData} object and triggers {@link GameOverListener#onLose()} methods.
     */
    public void loseLevel() {
        setState(ENDING);
        if (board.getState() == Board.SELECTING) board.abortSelection();
        board.setLocked(true);

        addStateListener((oldState, newState) -> {
            for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onLose();
            audioManager.clearBG();
            audioManager.playSFX("lose");
        });
    }

    /**
     * Finishes the game, writes necessary information about level completion into the {@link PlayerData} object
     * and triggers {@link GameOverListener#onWin(boolean)} methods.
     */
    public void winLevel() {
        setState(ENDING);
        if (board.getState() == Board.SELECTING) board.abortSelection();
        board.setLocked(true);

        LevelIdentifier level = levelData.getLevelIdentifier();
        boolean unlockedAbility;
        int stars = 1;
        if (countdown.getTurns() >= levelData.getThreeStarThreshold()) stars = 3;
        else if (countdown.getTurns() >= levelData.getTwoStarThreshold()) stars = 2;
        unlockedAbility = stars > 1 && !playerData.getUnlockedAbilities().contains(levelData.getRewardAbility());
        // Checking whether new information should be written.
        if (!playerData.isLevelCompleted(level) || countdown.getTurns() > playerData.getLevelTurnsLeft(level)) {
            playerData.setLevelBestResult(level, countdown.getTurns(), stars);
            if (levelData.getNextLevelIdentifier() != null && !playerData.isLevelUnlocked(levelData.getNextLevelIdentifier())) playerData.unlockLevel(levelData.getNextLevelIdentifier());
            if (unlockedAbility) playerData.unlockAbility(levelData.getRewardAbility());
            try {
                DataManager.savePlayerData(playerData);
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        addStateListener((oldState, newState) -> {
            for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onWin(unlockedAbility);
            audioManager.clearBG();
            audioManager.playSFX("win");
        });
    }

    public GamePanelGraphics getGameGraphics() {
        return graphics;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public Board getBoard() {
        return board;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0 || state > 3) throw new IllegalArgumentException("GamePanel does not support state " + state);
        if (state != this.state) {
            int oldState = this.state;
            this.state = state;
            for (StateListener listener : new ArrayList<>(stateListeners)) listener.onStateChanged(oldState, state);
        }
    }

    /**
     * Returns this GamePanel's {@link Countdown} object.
     *
     * @return countdown
     */
    public Countdown getCountdown() {
        return countdown;
    }

    public int getBaseTileDamage() {
        return baseTileDamage;
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public int getGameMode() {
        return gameMode;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void addGameOverListener(GameOverListener listener) {
        gameOverListeners.add(listener);
    }

    public void removeGameOverListener(GameOverListener listener) {
        gameOverListeners.remove(listener);
    }

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public void addUIDataListener(UIDataListener listener) {
        uiDataListeners.add(listener);
    }

    public void removeUIDataListener(UIDataListener listener) {
        uiDataListeners.remove(listener);
    }

}
