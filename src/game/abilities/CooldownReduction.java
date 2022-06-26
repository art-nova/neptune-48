package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "cooldownReduction" passive ability.
 *
 * @author Artem Novak
 */
public class CooldownReduction extends PassiveAbility {
    public static int REDUCTION_PERCENTAGE = 30;

    public CooldownReduction(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    @Override
    public String getNameID() {
        return "cooldownReduction";
    }

    /**
     * Reduces base cooldowns of all active abilities by {@link CooldownReduction#REDUCTION_PERCENTAGE} percent.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        Attack attack = abilityManager.getAttack();
        ActiveAbility active1 = abilityManager.getActive1();
        ActiveAbility active2 = abilityManager.getActive2();
        attack.setCooldown(attack.getCooldown() - (int)(attack.getCooldown()*REDUCTION_PERCENTAGE/100f));
        if (active1 != null) {
            active1.setCooldown(active1.getCooldown() - (int)(active1.getCooldown()*REDUCTION_PERCENTAGE/100f));
            if (active2 != null) active2.setCooldown(active2.getCooldown() - (int)(active2.getCooldown()*REDUCTION_PERCENTAGE/100f));
        }
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
