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
 * Class that implements functionality of "randomDispose" obstacle.
 *
 * @author Artem Novak
 */
public class RandomDispose extends Obstacle {
    private final Board board;
    private final Random random = new Random();

    private List<BoardCell> lastCheckCells = new ArrayList<>();

    public RandomDispose(GamePanel gp) {
        super(gp);
        this.board = gp.getBoard();
    }

    @Override
    public String getNameID() {
        return "randomDispose";
    }

    @Override
    public void startApplication() {
        BoardCell cell = lastCheckCells.get(random.nextInt(0, lastCheckCells.size()));
        Tile tile = board.getTileInCell(cell);
        board.lingerTile(tile);
        board.disposeCellContent(cell);
        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    board.removeStateListener(this);
                    board.unlingerTile(tile);
                    board.animateTileDisposal(tile);
                    board.setState(Board.ANIMATING);
                }
            }
        });
    }

    @Override
    protected boolean determineApplicability() {
        lastCheckCells = board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && tile.getLevel() > 0  && !tile.isLocked();
        });
        return !lastCheckCells.isEmpty();
    }
}
