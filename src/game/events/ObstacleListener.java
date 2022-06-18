package game.events;

/**
 * Interface that allows to register obstacle application events.
 *
 * @author Artem Novak
 */
public interface ObstacleListener {
    /**
     * Is triggered when obstacle is chosen to be applied, but not yet applied.
     *
     * @param e {@link ObstacleEvent} that holds information about the obstacle and allows to change it (obstacle from this event is applied)
     */
    void onObstacleSelected(ObstacleEvent e);
}
