package game.events;

/**
 * Interface that allows to register changes to a certain game modifier.
 *
 * @author Artem Novak
 * @deprecated
 */
public interface GameModifierListener {
    /**
     * Is triggered when the modifier's applicability changes.
     *
     * @param applicable whether modifier is applicable after the change
     */
    void onStateChanged(boolean applicable);

    /**
     * Is triggered when the modifier is actually applied to the game (may differ from application start for non-instant modifiers).
     */
    void onApplication();
}
