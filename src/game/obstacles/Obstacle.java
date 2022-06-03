package game.obstacles;

import game.GameModifier;
import game.GamePanel;

/**
 * Class that describes an abstract game obstacle.
 *
 * @author Artem Novak
 */
public abstract class Obstacle implements GameModifier {
    protected GamePanel gp;
    protected boolean applicable;
    protected boolean active;

    /**
     * Constructs an abstract obstacle.
     *
     * @param gp GamePanel to interact with
     */
    public Obstacle(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public boolean isApplicable() {
        return applicable;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Obstacle) {
            return ((Obstacle)o).getNameID().equals(getNameID());
        }
        return false;
    }
}
