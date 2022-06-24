package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;
import game.events.CellSelectionListener;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements functionality of "swap" active ability along with tile selection for it.
 *
 * @author Artem Novak
 */
public class Swap extends ActiveAbility {
    public static final int DEFAULT_COOLDOWN = 10;

    public Swap(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager, DEFAULT_COOLDOWN);
        determineApplicability();
    }

    @Override
    public String getNameID() {
        return "swap";
    }

    /**
     * Launches cell selection sequence of the board with subsequent swapping of contents.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        setState(APPLYING);
        board.initSelection(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && !tile.isLocked();
        }, 2);
        board.addCellSelectionListener(new CellSelectionListener() {
            @Override
            public void onSelectionUpdated(List<BoardCell> cells) {
            }

            @Override
            public void onSelectionCompleted(List<BoardCell> cells) {
                board.removeCellSelectionListener(this);
                BoardCell cell1 = cells.get(0);
                BoardCell cell2 = cells.get(1);
                Tile tile1 = board.getTileInCell(cell1);
                Tile tile2 = board.getTileInCell(cell2);
                board.swapCellContents(cell1, cell2);
                board.animateTileMove(tile1, cell2);
                board.animateTileMove(tile2, cell1);
                currentCooldown = cooldown;
                for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
            }

            @Override
            public void onSelectionAborted() {
                board.removeCellSelectionListener(this);
                updateApplicability();
            }
        });
    }

    @Override
    protected boolean determineApplicability() {
        return super.determineApplicability() && board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && !tile.isLocked();
        }).size() > 1;
    }
}
