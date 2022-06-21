package game.events;

/**
 * Interface that allows to register logical updates.
 *
 * @author Artem Novak
 */
public interface UpdateListener {
    /**
     * Is triggered right after the logical state is updated.
     * <br>
     * This is necessary to safely call logical methods of game objects out of sync (e.g. when a mouse is pressed on a button).
     *
     * @throws IllegalArgumentException if the event is nonexistent
     */
    void onUpdate();
}
