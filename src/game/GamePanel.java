package game;

import game.abilities.AbilityManager;
import game.events.*;
import game.gameobjects.Board;
import game.gameobjects.Entity;
import game.obstacles.ObstacleManager;
import game.utils.GamePanelGraphics;

import java.util.ArrayList;
import java.util.Map;
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
    // Game state
    public static final int PLAYING = 0, PAUSED = 1, ENDING = 2, ENDED = 3;
    // Game mode
    public static final int GAME_MODE_ATTACK = 0, GAME_MODE_REPAIR = 1;

    // How many logical updates and visual repaints are done per second
    private static final int FPS = 60;

    private final GamePanelGraphics graphics;
    private final KeyHandler keyHandler = new KeyHandler();
    private final MouseHandler mouseHandler = new MouseHandler();
    private final Thread gameThread = new Thread(this);
    private final Countdown countdown;
    private final Board board;
    private final Entity entity;
    private final ObstacleManager obstacleManager;
    private final AbilityManager abilityManager;
    private final List<GameOverListener> gameOverListeners = new ArrayList<>();
    private final List<StateListener> stateListeners = new ArrayList<>();
    private final List<UIDataListener> uiDataListeners = new ArrayList<>();
    private final List<UpdateListener> updateListeners = new ArrayList<>();
    private final int baseTileDamage;
    private final int gameMode;

    private int state = PLAYING;

    /**
     * Constructs a game panel with given graphics manager object.
     *
     * @param boardRows number of board rows
     * @param boardCols number of board columns
     * @param activeAbility1 NameID of the first active ability (or null if none selected)
     * @param activeAbility2 NameID of the second active ability (or null if none selected)
     * @param passiveAbility NameID of the passive ability (or null if none selected)
     * @param obstacleWeights map of obstacle NameIDs to their relative weights during selection
     * @param graphics non-loaded graphics manager object
     * @param baseFrame base {@link JFrame} of this panel
     * @param entityMaxHealth max health of the entity
     * @param entityIndex index of the entity to be loaded (in the entity texture folder)
     * @param time time in seconds after which level is considered failed
     * @param baseTileDamage damage dealt by level 1 tile
     * @param minObstacleInterval minimal interval in turns between obstacles
     * @param maxObstacleInterval maximal interval in turns between obstacles
     * @param gameMode int id of the game mode, {@link GamePanel#GAME_MODE_ATTACK} or {@link GamePanel#GAME_MODE_REPAIR}
     */
    public GamePanel(int boardRows, int boardCols, String activeAbility1, String activeAbility2, String passiveAbility, Map<String, Integer> obstacleWeights, GamePanelGraphics graphics, JFrame baseFrame,
                     int entityMaxHealth, int entityIndex, int time, int baseTileDamage, int minObstacleInterval, int maxObstacleInterval, int gameMode) throws IOException {
        this.countdown = new Countdown(time);
        this.baseTileDamage = baseTileDamage;
        this.graphics = graphics;
        this.gameMode = gameMode;
        this.entity = new Entity(0, 0, entityMaxHealth, this);
        this.board = new Board(0, graphics.getEntityHeight() + graphics.getEntityBoardDistance(), boardRows, boardCols, 1, this);
        this.obstacleManager = new ObstacleManager(obstacleWeights, minObstacleInterval, maxObstacleInterval, this);
        this.abilityManager = new AbilityManager(activeAbility1, activeAbility2, passiveAbility, this);
        baseFrame.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(graphics.getEntityWidth(), board.getPreferredHeight() + graphics.getEntityBoardDistance() + graphics.getEntityHeight()));
        board.setX((graphics.getEntityWidth() - board.getPreferredWidth())/2f);

        graphics.load(boardRows, boardCols, entityIndex, gameMode);
        board.generateRandomTile();
        board.generateRandomTile();
        gameThread.start();
    }

    @Override
    public void run() {
        double frameInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        countdown.addUIDataListener(() -> {if (countdown.getTime() <= 0) loseLevel();});
        countdown.start();

        while (state != ENDED) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/frameInterval;
            lastTime = currentTime;

            if ((state == PLAYING || state == ENDING) && delta >= 1) {
                for (int i = 0; i < (int)delta; i++) {
                    update();
                }
                repaint();
                delta -= (int)delta;

                if (state == ENDING && board.getState() == Board.IDLE && entity.getState() == Entity.IDLE) state = ENDED;
            }
        }
    }

    /**
     * Updates all components of the panel.
     */
    public void update() {
        if (state != ENDING) {
            countdown.update();
            abilityManager.update();
        }
        entity.update();
        board.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        entity.render(g2d);
        board.render(g2d);
        g.dispose();
    }

    public void loseLevel() {
        state = ENDING;
        if (board.getState() == Board.SELECTING) board.abortSelection();
        board.setLocked(true);
        for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onLose();
    }

    public void winLevel() {
        state = ENDING;
        if (board.getState() == Board.SELECTING) board.abortSelection();
        board.setLocked(true);
        for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onWin(countdown.getTime());
    }

    public GamePanelGraphics getGameGraphics() {
        return graphics;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
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

    public int getGameMode() {
        return gameMode;
    }

    /**
     * Schedules {@link UpdateListener#onUpdate()}'s application after only the next update's completion, disposing of it afterwards.
     * <br>
     * This is the preferred method of scheduling actions based on out-of-sync stimuli (such as player clicking a UI button).
     * 
     * @param scheduled implementation of {@link UpdateListener} that contains the scheduled sequence
     */
    public void scheduleAfterUpdate(UpdateListener scheduled) {
        updateListeners.add(new UpdateListener() {
            @Override
            public void onUpdate() {
                scheduled.onUpdate();
                updateListeners.remove(this);
            }
        });
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

    public void addUpdateListener(UpdateListener listener) {
        updateListeners.add(listener);
    }
    public void removeUpdateListener(UpdateListener listener) {
        updateListeners.remove(listener);
    }

}
