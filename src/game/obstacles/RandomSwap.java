package game.obstacles;

import game.GameModifier;
import game.GamePanel;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that implements functionality of "randomSwap" obstacle.
 *
 * @author Artem Novak
 */
public class RandomSwap extends Obstacle {
    private final Board board;
    private final Random random = new Random();

    private List<BoardCell> lastCheckCells = new ArrayList<>();

    public RandomSwap(GamePanel gp) {
        super(gp);
        board = gp.getBoard();
        board.addTurnListener(() -> {
            lastCheckCells = board.getCellsByPredicate(x -> board.getTileInCell(x) != null);
            if (lastCheckCells.size() < 2) setState(GameModifier.UNAPPLICABLE);
            else setState(GameModifier.APPLICABLE);
        });
    }

    @Override
    public String getNameID() {
        return "randomSwap";
    }

    @Override
    public void startApplication() {
        List<BoardCell> swappableCells = new ArrayList<>(lastCheckCells);
        int index1 = random.nextInt(0, swappableCells.size());
        BoardCell cell1 = swappableCells.get(index1);
        swappableCells.remove(index1);
        BoardCell cell2 = swappableCells.get(random.nextInt(0, swappableCells.size()));
        Tile tile1 = board.getTileInCell(cell1);
        Tile tile2 = board.getTileInCell(cell2);

        board.swapCellContents(cell1, cell2);
        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    board.removeStateListener(this);
                    board.animateTileMove(tile1, cell2);
                    board.animateTileMove(tile2, cell1);
                    board.setState(Board.ANIMATING);
                }
            }
        });
    }
}
