package game;

import game.gameobjects.Board;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class that implements methods for processing input keys for the game.
 *
 * @author Artem Novak
 */
public class KeyHandler extends KeyAdapter {
    private final GamePanel gp;
    private final Board board;
    private final ActionHandler actionHandler;

    /**
     * Constructs a KeyHandler.
     *
     * @param gp {@link GamePanel} which stores game-related information.
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
        this.board = gp.getBoard();
        this.actionHandler = gp.getActionHandler();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (gp.getState() == GamePanel.PLAYING) {
            if (board.getState() == Board.IDLE || board.getState() == Board.ANIMATING) {
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) actionHandler.scheduleAction("up");
                else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) actionHandler.scheduleAction("down");
                else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) actionHandler.scheduleAction("left");
                else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) actionHandler.scheduleAction("right");
                else if (key == KeyEvent.VK_SPACE) actionHandler.scheduleAction("attack");
                else if (key == KeyEvent.VK_1) actionHandler.scheduleAction("active1");
                else if (key == KeyEvent.VK_2) actionHandler.scheduleAction("active2");
                else if (key == KeyEvent.VK_ESCAPE) actionHandler.scheduleAction("pause");
            }
            else if (board.getState() == Board.SELECTING) {
                if (key == KeyEvent.VK_SPACE) actionHandler.scheduleAction("attack");
                else if (key == KeyEvent.VK_1) actionHandler.scheduleAction("active1");
                else if (key == KeyEvent.VK_2) actionHandler.scheduleAction("active2");
                else if (key == KeyEvent.VK_ESCAPE) actionHandler.scheduleAction("abortSelection");
            }
        }
        else if (gp.getState() == GamePanel.PAUSED && key == KeyEvent.VK_ESCAPE) actionHandler.scheduleAction("unpause");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) actionHandler.clearAction("up");
        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) actionHandler.clearAction("down");
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) actionHandler.clearAction("left");
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) actionHandler.clearAction("right");
        else if (key == KeyEvent.VK_SPACE) actionHandler.clearAction("attack");
        else if (key == KeyEvent.VK_1) actionHandler.clearAction("active1");
        else if (key == KeyEvent.VK_2) actionHandler.clearAction("active2");
        else if (key == KeyEvent.VK_ESCAPE) actionHandler.clearAction("pause");
        if (key == KeyEvent.VK_SPACE) actionHandler.clearAction("attack");
        else if (key == KeyEvent.VK_1) actionHandler.clearAction("active1");
        else if (key == KeyEvent.VK_2) actionHandler.clearAction("active2");
        else if (key == KeyEvent.VK_ESCAPE) {
            actionHandler.clearAction("abortSelection");
            actionHandler.clearAction("pause");
            actionHandler.clearAction("unpause");
        }
    }
}
