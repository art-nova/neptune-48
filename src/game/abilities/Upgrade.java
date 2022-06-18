package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;
import game.events.CellSelectionListener;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements functionality of "upgrade" active ability along with tile selection for it.
 *
 * @author Artem Novak
 */
public class Upgrade extends ActiveAbility {
    public static final int DEFAULT_COOLDOWN = 5;

    public Upgrade(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager, DEFAULT_COOLDOWN);
        determineApplicability();
    }

    @Override
    public String getNameID() {
        return "upgrade";
    }

    /**
     * Launches cell selection sequence of the board with subsequent upgrading of the tile.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        setState(APPLYING);
        board.initSelection(x -> board.getTileInCell(x) != null, 1);
        board.addCellSelectionListener(new CellSelectionListener() {
            @Override
            public void onSelectionUpdated(List<BoardCell> cells) {

            }

            @Override
            public void onSelectionCompleted(List<BoardCell> cells) {
                board.removeCellSelectionListener(this);
                Tile tile = board.getTileInCell(cells.get(0));
                tile.setLevel(tile.getLevel() + 1);
                tile.upgradeAnimation();
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
        return super.determineApplicability() && !board.getCellsByPredicate(x -> board.getTileInCell(x) != null).isEmpty();
    }
}
