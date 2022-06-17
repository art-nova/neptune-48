package game.obstacles;

import game.GameModifier;
import game.GamePanel;
import game.events.StateListener;
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
        board.addTurnListener(() -> {
            lastCheckCells = board.getCellsByPredicate(x -> board.getTileInCell(x) == null);
            if (lastCheckCells.isEmpty()) setState(GameModifier.UNAPPLICABLE);
            else setState(GameModifier.APPLICABLE);
        });
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
}
