package game.events;

/**
 * Interface that allows to register entity-specific events.
 *
 * @author Artem Novak
 */
public interface EntityListener {
    /**
     * Is triggered when health is changed.
     *
     * @param oldHealth old health
     * @param newHealth new health
     */
    void onHealthChanged(int oldHealth, int newHealth);
}
