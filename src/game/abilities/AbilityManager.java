package game.abilities;

import game.GameLogicException;
import game.GameModifier;
import game.GamePanel;
import game.KeyHandler;
import game.events.AbilityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles application of abilities.
 *
 * @author Artem Novak
 */
public class AbilityManager {

    private final Attack attack;
    private final ActiveAbility active1;
    private final ActiveAbility active2;
    private final PassiveAbility passive;
    private final GamePanel gp;
    private final KeyHandler keyHandler;

    /**
     * Constructs a new BonusManager with given abilities selected for the level.
     *
     * @param active1 NameID of the first selected active ability (or null if no such ability selected)
     * @param active2 NameID of the second selected active ability (or null if no such ability selected)
     * @param passive NameID of the selected passive ability (or null if no such ability selected)
     * @param gp base {@link GamePanel}
     */
    public AbilityManager(String active1, String active2, String passive, GamePanel gp) {
        if (active1 == null && active2 != null) throw new GameLogicException("Trying to pass an active ability into the second slot while the first is empty");
        this.gp = gp;
        this.keyHandler = gp.getKeyHandler();
        this.attack = new Attack(gp, this);
        this.active1 = registerActiveAbility(active1);
        this.active2 = registerActiveAbility(active2);
        this.passive = registerPassiveAbility(passive);
        AbilityListener updateApplicability = () -> {
            if (this.active1 != null) {
                this.active1.updateApplicability();
                if (this.active2 != null) this.active2.updateApplicability();
            }
        };
        if (this.active1 != null) this.active1.addAbilityListener(updateApplicability);
        if (this.active2 != null) this.active2.addAbilityListener(updateApplicability);
        if (this.passive != null) this.passive.startApplication();
    }

    /**
     * Monitors keyboard ability activation.
     */
    public void update() {
        if (keyHandler.isKeyPressed()) {
            if (keyHandler.getLastPressKey().equals("attack") && attack.getState() == GameModifier.APPLICABLE) attack.startApplication();
            if (active1 != null) {
                if (keyHandler.getLastPressKey().equals("active1") && active1.getState() == GameModifier.APPLICABLE) active1.startApplication();
                else if (active2 != null && keyHandler.getLastPressKey().equals("active2") && active2.getState() == GameModifier.APPLICABLE) active2.startApplication();
            }
        }
    }

    public Attack getAttack() {
        return attack;
    }

    public ActiveAbility getActive1() {
        return active1;
    }

    public ActiveAbility getActive2() {
        return active2;
    }

    public PassiveAbility getPassive() {
        return passive;
    }

    private PassiveAbility registerPassiveAbility(String nameID) {
        if (nameID == null) return null;
        return switch (nameID) {
            case "betterBaseLevel" -> new BetterBaseLevel(gp, this);
            case "bonusDamage" -> new BonusDamage(gp, this);
            case "bonusTime" -> new BonusTime(gp, this);
            case "cooldownReduction" -> new CooldownReduction(gp, this);
            case "resistance" -> new Resistance(gp, this);
            default -> throw new IllegalArgumentException("Passive ability " + nameID + " does not exist");
        };
    }

    private ActiveAbility registerActiveAbility(String nameID) {
        if (nameID == null) return null;
        return switch (nameID) {
            case "crit" -> null; // PLACEHOLDER
            case "dispose" -> null; // PLACEHOLDER
            case "merge" -> null; // PLACEHOLDER
            case "safeAttack" -> null; // PLACEHOLDER
            case "scramble" -> null; // PLACEHOLDER
            case "swap" -> null; // PLACEHOLDER
            case "upgrade" -> null; //PLACEHOLDER
            default -> throw new IllegalArgumentException("Active ability " + nameID + " does not exist");
        };
    }
}
