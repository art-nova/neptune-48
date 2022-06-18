package game.events;

import game.obstacles.Obstacle;

/**
 * Class that holds information about an obstacle occurrence.
 *
 * @author Artem Novak
 */
public class ObstacleEvent {
    private Obstacle obstacle;

    public ObstacleEvent(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }
}
