package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "bonusTime" passive ability.
 *
 * @author Artem Novak
 */
public class BonusTime extends PassiveAbility {
    public static final int ADDED_PERCENTAGE = 30;

    public BonusTime(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    @Override
    public String getNameID() {
        return "bonusTime";
    }

    /**
     * Increases time dedicated for level completion by {@link BonusTime#ADDED_PERCENTAGE} percent.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        gp.getCountdown().offsetTime((int)(gp.getCountdown().getDedicatedTime() * ADDED_PERCENTAGE / 100f));
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
