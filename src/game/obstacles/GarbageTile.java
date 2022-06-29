package game.obstacles;

import game.GamePanel;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that implements functionality of "garbageTile" obstacle.
 *
 * @author Artem Novak
 */
public class GarbageTile extends Obstacle {
    private final Board board;
    private final Random random = new Random();

    private List<BoardCell> lastCheckCells = new ArrayList<>();

    public GarbageTile(GamePanel gp) {
        super(gp);
        this.board = gp.getBoard();
    }

    @Override
    public String getNameID() {
        return "garbageTile";
    }

    @Override
    public void startApplication() {
        BoardCell cell = lastCheckCells.get(random.nextInt(0, lastCheckCells.size()));
        board.generateTile(cell, 0);
    }

    @Override
    protected boolean determineApplicability() {
        lastCheckCells = board.getCellsByPredicate(x -> board.getTileInCell(x) == null);
        return super.determineApplicability() && !lastCheckCells.isEmpty();
    }
}
