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
     * Is triggered when game is won via any condition.
     *
     * @param timeLeft time left before losing (can be used for score calculation)
     */
    void onWin(int timeLeft);
}
