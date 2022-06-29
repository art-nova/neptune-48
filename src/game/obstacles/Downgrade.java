package game.obstacles;

import game.GamePanel;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that implements "downgrade" obstacle.
 *
 * @author Artem Novak
 */
public class Downgrade extends Obstacle{
    private final Board board;
    private final Random random = new Random();

    private List<BoardCell> lastCheckCells = new ArrayList<>();

    public Downgrade(GamePanel gp) {
        super(gp);
        board = gp.getBoard();
    }

    @Override
    public String getNameID() {
        return "downgrade";
    }

    @Override
    public void startApplication() {
        BoardCell cell = lastCheckCells.get(random.nextInt(0, lastCheckCells.size()));
        Tile tile = board.getTileInCell(cell);
        tile.setLevel(tile.getLevel() - 1);
        tile.setLevelVisualOffset(tile.getLevelVisualOffset()+1);
        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    board.removeStateListener(this);
                    tile.setLevelVisualOffset(0);
                    tile.downgradeAnimation();
                    board.setState(Board.ANIMATING);
                }
            }
        });
    }

    @Override
    protected boolean determineApplicability() {
        lastCheckCells = board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && tile.getLevel() > 1 && !tile.isLocked();
        });
        return super.determineApplicability() && !lastCheckCells.isEmpty();
    }
}
