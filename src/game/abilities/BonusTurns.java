package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "bonusTurns" passive ability.
 *
 * @author Artem Novak
 */
public class BonusTurns extends PassiveAbility {
    public static final int ADDED_PERCENTAGE = 30;

    public BonusTurns(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    @Override
    public String getNameID() {
        return "bonusTurns";
    }

    /**
     * Increases turns dedicated for level completion by {@link BonusTurns#ADDED_PERCENTAGE} percent.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        gp.getCountdown().offsetTurns((int)(gp.getCountdown().getDedicatedTurns() * ADDED_PERCENTAGE / 100f));
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
