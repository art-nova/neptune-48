package game.events;

/**
 * Interface that allows to monitor animation-specific events.
 *
 * @author Artem Novak
 */
public interface AnimationListener {
    /**
     * Is triggered when object's animation ends.
     */
    void onAnimationOver();
}
