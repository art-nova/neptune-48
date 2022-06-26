package game.abilities;

import game.GameLogicException;
import game.GameModifier;
import game.GamePanel;
import game.ActionHandler;
import game.events.AbilityListener;
import game.gameobjects.Board;

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
    private final ActionHandler actionHandler;
    private final Board board;

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
        this.board = gp.getBoard();
        this.actionHandler = gp.getActionHandler();
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
        if (actionHandler.anyActionScheduled()) {
            switch (actionHandler.getPriorityAction()) {
                case "attack" -> attemptAttack();
                case "active1" -> attemptActive1();
                case "active2" -> attemptActive2();
            }
        }
    }

    /**
     * Attempts to apply attack (respecting all the non-application conditions).
     * <br>
     * Preferred method for outside access.
     */
    public void attemptAttack() {
        actionHandler.clearAction("attack");
        if (!board.isLocked() && attack.getState() == GameModifier.APPLICABLE) attack.startApplication();
    }

    /**
     * Attempts to apply active ability 1 (respecting all the non-application conditions).
     * <br>
     * Preferred method for outside access.
     */
    public void attemptActive1() {
        actionHandler.clearAction("active1");
        if (active1 != null && !board.isLocked() && active1.getState() == GameModifier.APPLICABLE) active1.startApplication();
    }

    /**
     * Attempts to apply active ability 2 (respecting all the non-application conditions).
     * <br>
     * Preferred method for outside access.
     */
    public void attemptActive2() {
        actionHandler.clearAction("active2");
        if (active2 != null && !board.isLocked() && active2.getState() == GameModifier.APPLICABLE) active2.startApplication();
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
            case "bonusTurns" -> new BonusTurns(gp, this);
            case "cooldownReduction" -> new CooldownReduction(gp, this);
            case "resistance" -> new Resistance(gp, this);
            default -> throw new IllegalArgumentException("Passive ability " + nameID + " does not exist");
        };
    }

    private ActiveAbility registerActiveAbility(String nameID) {
        if (nameID == null) return null;
        return switch (nameID) {
            case "crit" -> new Crit(gp, this);
            case "dispose" -> new Dispose(gp, this);
            case "merge" -> new Merge(gp, this);
            case "safeAttack" -> new SafeAttack(gp, this);
            case "scramble" -> new Scramble(gp, this);
            case "swap" -> new Swap(gp, this);
            case "upgrade" -> new Upgrade(gp, this);
            default -> throw new IllegalArgumentException("Active ability " + nameID + " does not exist");
        };
    }
}
