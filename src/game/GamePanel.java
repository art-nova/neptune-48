package game;

import game.bonuses.BonusManager;
import game.utils.GamePanelGraphics;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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

    public int state = PLAYING;
    // Time left in seconds
    public int timeLeft;
    public final Board board;
    public final GamePanelGraphics graphicsManager;
    public final KeyHandler keyHandler = new KeyHandler();
    public final MouseHandler mouseHandler = new MouseHandler();

    private static final int FPS = 60;
    private final Thread gameThread = new Thread(this);

    /**
     * Constructs a game panel with given graphics manager object.
     *
     * @param boardRows number of board rows
     * @param boardCols number of board columns
     * @param bonusNameIDs NameIDs of selected bonuses
     * @param obstacleNameIDs NameIDs of level's obstacles
     * @param graphicsManager non-loaded graphics manager object
     * @param time time in seconds after which level is considered failed
     */
    public GamePanel(int boardRows, int boardCols, Set<String> bonusNameIDs, Set<String> obstacleNameIDs, GamePanelGraphics graphicsManager, int time) throws IOException {
        this.graphicsManager = graphicsManager;
        this.board = new Board(boardRows, boardCols, 1, this);
        UIManager.getFrame().addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(board.preferredWidth, board.preferredHeight));
        this.timeLeft = time;

        graphicsManager.load(boardRows, boardCols);
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

            if ((state == PLAYING || state == ENDING) && delta >= 1) {
                for (int i = 0; i < (int)delta; i++) {
                    update();

                    if (secondsTimer >= 1000000000) {
                        secondsTimer = 0;
                        timeLeft--;
                        if (timeLeft <= 0) loseLevel();
                    }
                }
                repaint();
                delta -= (int)delta;

                if (state == ENDING && board.state == Board.STATIC) state = ENDED;
            }
        }
    }

    /**
     * Updates all components of the panel.
     */
    public void update() {
        board.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.render((Graphics2D)g);
        g.dispose();
    }

    public void loseLevel() {
        System.out.println("You lost");
        state = ENDING;
        // TODO
    }

    public void reactToTurn() {
        // TODO
    }

}
