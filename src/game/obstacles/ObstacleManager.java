package game.obstacles;

import game.GamePanel;

import java.util.Set;

public class ObstacleManager {

    public final Set<String> obstacles;

    public ObstacleManager(Set<String> obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Creates a new obstacle from its NameID and supplies given GamePanel as its target.
     *
     * @param nameID obstacle NameID
     * @param gp GamePanel to interact with
     * @return newly instantiated obstacle of corresponding class, cast to parent class Obstacle.
     */
    public static Obstacle newObstacleByNameID(String nameID, GamePanel gp) {
        IllegalArgumentException badObstacle = new IllegalArgumentException("Could not find obstacle!");
        if (nameID == null || nameID.isEmpty()) throw badObstacle;
        try {
            String className = "game.obstacles."+Character.toUpperCase(nameID.charAt(0))+nameID.substring(1);
            return (Obstacle)Class.forName(className).getConstructor(GamePanel.class).newInstance(gp);
        } catch (Exception e) {
            throw badObstacle;
        }
    }
}
