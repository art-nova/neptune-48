package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "betterBaseLevel" passive ability.
 *
 * @author Artem Novak
 */
public class BetterBaseLevel extends PassiveAbility {
    public static final int LEVEL_INCREASE = 1;

    public BetterBaseLevel(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    @Override
    public String getNameID() {
        return "betterBaseLevel";
    }

    /**
     * Increases base tile level of the board by {@link BetterBaseLevel#LEVEL_INCREASE}.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        gp.getBoard().setBaseTileLevel(gp.getBoard().getBaseTileLevel() + LEVEL_INCREASE);
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
