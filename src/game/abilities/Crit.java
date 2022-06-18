package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;
import game.events.AttackEvent;
import game.events.AttackListener;

import java.util.ArrayList;

/**
 * Class that implements functionality of "crit" active ability along with tile selection for it.
 *
 * @author Artem Novak
 */
public class Crit extends ActiveAbility {
    public static final int DEFAULT_COOLDOWN = 20;
    public static final int DAMAGE_ADDED_PERCENTAGE = 200;

    private final Attack attack;

    public Crit(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager, DEFAULT_COOLDOWN);
        this.attack = abilityManager.getAttack();
    }

    @Override
    public String getNameID() {
        return "crit";
    }

    /**
     * Adds {@link Crit#DAMAGE_ADDED_PERCENTAGE} percent damage bonus to the next attack and remains non-applicable until it occurs.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        setState(APPLYING);
        attack.addAttackListener(new AttackListener() {
            @Override
            public void onAttack(AttackEvent e) {
                attack.removeAttackListener(this);
                e.offsetDamagePercent(DAMAGE_ADDED_PERCENTAGE);
                currentCooldown = cooldown;
                for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
            }
        });
    }
}
