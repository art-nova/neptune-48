package game.events;

import game.obstacles.Obstacle;

/**
 * Abstract class that holds information about an obstacle that is about to or has occurred.
 * <br>
 * The primary purpose of this class is to allow outside injections into obstacle application process.
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
