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
    public Board board;
    public GamePanelGraphics graphicsManager;
    public final KeyHandler keyHandler = new KeyHandler();
    public final MouseHandler mouseHandler = new MouseHandler();
    public boolean debug = false;

    private final Thread gameThread = new Thread(this);
    private static final int FPS = 60;

    /**
     * Constructs a game panel with given graphics manager object.
     *
     * @param boardRows number of board rows
     * @param boardCols number of board columns
     * @param bonusNameIDs NameIDs of selected bonuses
     * @param obstacleNameIDs NameIDs of level's obstacles
     * @param graphicsManager non-loaded graphics manager object
     */
    public GamePanel(int boardRows, int boardCols, Set<String> bonusNameIDs, Set<String> obstacleNameIDs, GamePanelGraphics graphicsManager) throws IOException {
        this.graphicsManager = graphicsManager;
        this.board = new Board(boardRows, boardCols, 1, this);
        UIManager.getFrame().addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
//        board.generateRandomTile();
//        board.generateRandomTile();
        board.generateTile(new BoardCell(0, 0), 1);
        board.generateTile(new BoardCell(1, 0), 2);
        board.generateTile(new BoardCell(2, 0), 3);
        board.generateTile(new BoardCell(3, 0), 4);
        board.generateTile(new BoardCell(0, 1), 5);
        board.generateTile(new BoardCell(1, 1), 6);
        board.generateTile(new BoardCell(2, 1), 7);
        board.generateTile(new BoardCell(3, 1), 8);
        board.generateTile(new BoardCell(0, 2), 1);
        board.generateTile(new BoardCell(1, 2), 2);
        board.generateTile(new BoardCell(2, 2), 3);
        board.generateTile(new BoardCell(3, 2), 4);
        board.generateTile(new BoardCell(0, 3), 5);
        board.generateTile(new BoardCell(1, 3), 6);
        board.generateTile(new BoardCell(2, 3), 7);
        this.setPreferredSize(new Dimension(board.preferredWidth, board.preferredHeight));
        graphicsManager.load(boardRows, boardCols);
        gameThread.start();
        board.initSelection(x -> {
            Tile tile = board.tileByBoardCell(x);
            return tile != null && tile.level == 1;
        }, 1);
    }

    @Override
    public void run() {
        double frameInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // Debug
        long debugTimer = 0;
        int frameCount = 0;

        while (state != ENDED) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/frameInterval;
            debugTimer += currentTime - lastTime; // DEBUG
            lastTime = currentTime;

            if ((state == PLAYING || state == ENDING) && delta >= 1) {
                for (int i = 0; i < (int)delta; i++) {
                    update();
                }
                repaint();
                frameCount++; // DEBUG
                delta -= (int)delta;

                if (state == ENDING && board.state == Board.STATIC) state = ENDED;
            }

            // DEBUG
            if (debugTimer >= 1000000000) {
                if (debug) System.out.println("FPS: " + frameCount);
                frameCount = 0;
                debugTimer = 0;
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
