package game.events;

/**
 * Interface that allows to register end of the game.
 *
 * @author Artem Novak
 */
public interface GameOverListener {
    /**
     * Is triggered when game is lost via any condition.
     */
    void onLose();

    /**
     * Is triggered when game is won via any condition, after writing the necessary logical information to {@link data.PlayerData}.
     *
     * @param abilityUnlocked whether the ability unlock criteria was met for the first time during this win
     */
    void onWin(boolean abilityUnlocked);
}
