package game.events;

/**
 * Interface that allows to register object state changes.
 *
 * @author Artem Novak
 */
public interface StateListener {
    /**
     * Is triggered after an object's state changes.
     *
     * @param oldState old state
     * @param newState new state
     */
    void onStateChanged(int oldState, int newState);
}
