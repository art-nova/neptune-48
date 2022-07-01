package game.abilities;

import UI.LevelMenu;
import game.GameLogicException;
import game.GamePanel;
import game.gameobjects.Board;

/**
 * Class that implements common elements of active abilities (actively triggered by player), namely cooldown.
 *
 * @author Artem Novak
 */
public abstract class ActiveAbility extends Ability {
    protected final LevelMenu.Ability updatedElement;
    protected final Board board;
    protected final AbilityManager manager;

    protected int cooldown;
    protected int currentCooldown;

    public ActiveAbility(GamePanel gp, AbilityManager abilityManager, int defaultCooldown, LevelMenu.Ability updatedElement) {
        super(gp, abilityManager);
        this.board = gp.getBoard();
        this.manager = gp.getAbilityManager();
        this.cooldown = defaultCooldown;
        this.updatedElement = updatedElement;
        // Determines whether the ability is applicable after a turn.
        board.addTurnListener(() -> {
            if (currentCooldown > 0) setCurrentCooldown(currentCooldown - 1); // Determines applicability because of currentCooldown change.
            else updateApplicability();
        });
    }

    /**
     * Method that implements common application start functionality for active abilities.
     */
    @Override
    public void startApplication() {
        if (board.getState() == Board.SELECTING) board.abortSelection();
    }

    /**
     * Gets current ability cooldown.
     *
     * @return ability cooldown (int turns)
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Changes cooldown that is applied to the ability after usage. Has no effect on cooldown that is currently in effect.
     *
     * @param cooldown new ability cooldown (int turns)
     */
    public void setCooldown(int cooldown) {
        if (cooldown < 0) throw new GameLogicException("Trying to set ability cooldown less than 0: " + cooldown);
        this.cooldown = cooldown;
    }

    /**
     * Gets the number of turns remaining before this ability may be used again.
     *
     * @return current cooldown (in turns)
     */
    public int getCurrentCooldown() {
        return currentCooldown;
    }

    /**
     * Changes the number of turns remaining before this ability may be used again.
     *
     * @param currentCooldown changed current cooldown
     */
    public void setCurrentCooldown(int currentCooldown) {
        if (currentCooldown < 0) throw new GameLogicException("Trying to set current ability cooldown less than 0: " + currentCooldown);
        if (this.currentCooldown != currentCooldown) {
            this.currentCooldown = currentCooldown;
            if (determineApplicability()) setState(APPLICABLE);
            else setState(UNAPPLICABLE);
        }
    }

    @Override
    public void setState(int state) {
        super.setState(state);
        if (state == APPLICABLE) updatedElement.removeCover();
        else updatedElement.setCover(currentCooldown);
    }

    @Override
    protected boolean determineApplicability() {
        return !board.isLocked() && currentCooldown == 0;
    }
}
