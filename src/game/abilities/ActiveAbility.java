package game.abilities;

import game.GameLogicException;
import game.GamePanel;
import game.events.UIDataListener;
import game.gameobjects.Board;

import java.util.ArrayList;

/**
 * Class that implements common elements of active abilities (actively triggered by player), namely cooldown.
 *
 * @author Artem Novak
 */
public abstract class ActiveAbility extends Ability {
    protected final Board board;
    protected final AbilityManager manager;

    protected int cooldown;
    protected int currentCooldown;

    public ActiveAbility(GamePanel gp, AbilityManager abilityManager, int defaultCooldown) {
        super(gp, abilityManager);
        this.board = gp.getBoard();
        this.manager = gp.getAbilityManager();
        this.cooldown = defaultCooldown;
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
            if (determineApplicability()) state = APPLICABLE;
            else state = UNAPPLICABLE;
            for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
        }
    }

    @Override
    protected boolean determineApplicability() {
        return currentCooldown == 0;
    }
}
