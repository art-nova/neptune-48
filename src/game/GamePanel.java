package game;

import game.bonuses.BonusManager;
import game.events.*;
import game.utils.GamePanelGraphics;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import UI.UIManager;

/**
 * Implementation of the panel in which the core game loop (2048 gameplay, attacking, triggering bonuses etc.) happens.
 *
 * @author Artem Novak
 */
public class GamePanel extends JPanel implements Runnable {
    // Game state
    public static final int PLAYING = 0, PAUSED = 1, ENDING = 2, ENDED = 3;

    // How many logical updates and visual repaints are done per second
    private static final int FPS = 60;

    private final GamePanelGraphics graphics;
    private final KeyHandler keyHandler = new KeyHandler();
    private final MouseHandler mouseHandler = new MouseHandler();
    private final Thread gameThread = new Thread(this);
    private final Board board;
    private final Entity entity;
    private final Attack attack;
    private final ArrayList<TimeListener> timeListeners = new ArrayList<>();
    private final ArrayList<GameOverListener> gameOverListeners = new ArrayList<>();
    private final ArrayList<StateListener> stateListeners = new ArrayList<>();
    private final int baseTileDamage;

    private int state = PLAYING;
    // Time left in seconds
    private int timeLeft;

    /**
     * Constructs a game panel with given graphics manager object.
     *
     * @param boardRows number of board rows
     * @param boardCols number of board columns
     * @param bonusNameIDs NameIDs of selected bonuses
     * @param obstacleNameIDs NameIDs of level's obstacles
     * @param graphics non-loaded graphics manager object
     * @param entityMaxHealth max health of the entity
     * @param entityIndex index of the entity to be loaded (in the entity texture folder)
     * @param time time in seconds after which level is considered failed
     * @param baseTileDamage damage dealt by level 1 tile
     */
    public GamePanel(int boardRows, int boardCols, Set<String> bonusNameIDs, Set<String> obstacleNameIDs, GamePanelGraphics graphics,
                     int entityMaxHealth, int entityIndex, int time, int baseTileDamage) throws IOException {
        this.graphics = graphics;
        this.entity = new Entity(0, 0, entityMaxHealth, this);
        this.board = new Board(0, GamePanelGraphics.ENTITY_HEIGHT + GamePanelGraphics.ENTITY_BOARD_DISTANCE, boardRows, boardCols, 1, this);
        this.attack = new Attack(this);
        UIManager.getFrame().addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(GamePanelGraphics.ENTITY_WIDTH, board.getPreferredHeight() + GamePanelGraphics.ENTITY_BOARD_DISTANCE + GamePanelGraphics.ENTITY_HEIGHT));
        board.setX((GamePanelGraphics.ENTITY_WIDTH - board.getPreferredWidth())/2f);
        this.timeLeft = time;
        this.baseTileDamage = baseTileDamage;

        graphics.load(boardRows, boardCols, entityIndex);
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

        long secondsTimer = 0;

        while (state != ENDED) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/frameInterval;
            secondsTimer += currentTime - lastTime;
            lastTime = currentTime;
            if (secondsTimer >= 1000000000 && state == PLAYING) {
                secondsTimer = 0;
                timeLeft--;
                for (TimeListener listener : new ArrayList<>(timeListeners)) listener.onTimeChanged(timeLeft + 1, timeLeft);
                if (timeLeft <= 0) loseLevel();
            }

            if ((state == PLAYING || state == ENDING) && delta >= 1) {
                for (int i = 0; i < (int)delta; i++) {
                    update();
                }
                repaint();
                delta -= (int)delta;

                if (state == ENDING && board.getState() == Board.IDLE) state = ENDED;
            }
        }
    }

    /**
     * Updates all components of the panel.
     */
    public void update() {
        entity.update();
        board.update();
        attack.update();
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
        for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onLose();
    }

    public void winLevel() {
        state = ENDING;
        for (GameOverListener listener : new ArrayList<>(gameOverListeners)) listener.onWin(timeLeft);
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

    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Offsets remaining time in seconds by given number. The number is added.
     * If the result of the offset is less than 0, sets remaining time to 0 instead.
     *
     * @param delta time change in seconds
     */
    public void offsetTimeLeft(int delta) {
        int oldTimeLeft = this.timeLeft;
        this.timeLeft = Math.max(timeLeft + delta, 0);
        for (TimeListener listener : new ArrayList<>(timeListeners)) listener.onTimeChanged(oldTimeLeft, this.timeLeft);
    }

    public int getBaseTileDamage() {
        return baseTileDamage;
    }

    public void addTimeListener(TimeListener listener) {
        timeListeners.add(listener);
    }

    public void removeTimeListener(TimeListener listener) {
        timeListeners.remove(listener);
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

}
