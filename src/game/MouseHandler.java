package game;

import game.gameobjects.Board;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class that implements methods for scheduling mouse-related game actions, namely tile selection.
 *
 * @author Artem Novak
 */
public class MouseHandler extends MouseAdapter {
    private final GamePanel gp;
    private final Board board;
    private final ActionHandler actionHandler;

    /**
     * Constructs a MouseHandler.
     *
     * @param gp {@link GamePanel} which stores game-related information.
     */
    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        this.board = gp.getBoard();
        this.actionHandler = gp.getActionHandler();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.getState() == GamePanel.PLAYING && board.getState() == Board.SELECTING) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                actionHandler.scheduleAction("selectTile");
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
                actionHandler.scheduleAction("abortSelection");
            }
        }
    }

}
