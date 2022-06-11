package game.events;

/**
 * Interface that allows to register time changes.
 *
 * @author Artem Novak
 */
public interface TimeListener {
    /**
     * Is triggered when time is changed.
     *
     * @param oldTime old time
     * @param newTime new time
     */
    void onTimeChanged(int oldTime, int newTime);
}
