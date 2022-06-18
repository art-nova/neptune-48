package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "bonusDamage" passive ability.
 *
 * @author Artem Novak
 */
public class BonusDamage extends PassiveAbility {
    public static final int ADDED_PERCENTAGE = 50;

    public BonusDamage(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    @Override
    public String getNameID() {
        return "bonusDamage";
    }

    /**
     * Increases damage dealt by {@link BonusDamage#ADDED_PERCENTAGE} percent.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        abilityManager.getAttack().addAttackListener(x -> x.offsetDamagePercent(ADDED_PERCENTAGE));
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
