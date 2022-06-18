package game.events;

/**
 * Interface that allows to register ability application events.
 *
 * @author Artem Novak
 */
public interface AbilityListener {
    /**
     * Is triggered by an ability right after it is applied.
     */
    void onAbilityApplied();
}
