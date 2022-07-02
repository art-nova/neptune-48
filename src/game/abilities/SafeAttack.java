package game.abilities;

import UI.LevelMenu;
import game.GamePanel;
import game.events.AbilityListener;
import game.events.AttackEvent;
import game.events.AttackListener;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.ArrayList;

/**
 * Class that implements functionality of "safeAttack" active ability along with tile selection for it.
 *
 * @author Artem Novak
 */
public class SafeAttack extends ActiveAbility {
    public static final int DEFAULT_COOLDOWN = 40;

    private final Attack attack;

    public SafeAttack(GamePanel gp, AbilityManager abilityManager, LevelMenu.Ability updatedElement) {
        super(gp, abilityManager, DEFAULT_COOLDOWN, updatedElement);
        attack = abilityManager.getAttack();
        updateApplicability();
    }

    @Override
    public String getNameID() {
        return "safeAttack";
    }

    @Override
    public void startApplication() {
        super.startApplication();
        setState(APPLYING);
        attack.addAttackListener(new AttackListener() {
            @Override
            public void onAttack(AttackEvent e) {
                attack.removeAttackListener(this);
                BoardCell cell = e.getOriginCell();
                Tile tile = e.getTile();
                board.unlingerTile(tile);
                board.putTileInCell(tile, cell);
                board.offsetTileCount(1);
                board.addStateListener(new StateListener() {
                    @Override
                    public void onStateChanged(int oldState, int newState) {
                        if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                            board.removeStateListener(this);
                            board.animateTileMove(tile, cell);
                            board.setState(Board.ANIMATING);
                        }
                    }
                });
                currentCooldown = cooldown;
                for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
            }
        });
    }
}
