package game.abilities;

import game.GameLogicException;
import game.GamePanel;

/**
 * Class that implements common elements of passive abilities (applied at level start and only once).
 *
 * @author Artem Novak
 */
public abstract class PassiveAbility extends Ability {
    protected boolean applied;

    public PassiveAbility(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
    }

    /**
     * Method that implements common application start functionality for passive abilities.
     */
    @Override
    public void startApplication() {
        if (applied) throw new GameLogicException("Trying to apply a passive bonus " + getNameID() + " twice");
        applied = true;
    }

    @Override
    protected boolean determineApplicability() {
        return true;
    }

}
