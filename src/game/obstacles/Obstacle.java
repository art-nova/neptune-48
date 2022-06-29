package game.obstacles;

import game.GameModifier;
import game.GamePanel;

/**
 * Class that describes an abstract game obstacle.
 *
 * @author Artem Novak
 */
public abstract class Obstacle extends GameModifier {
    /**
     * Constructs an abstract obstacle.
     *
     * @param gp GamePanel to interact with
     */
    public Obstacle(GamePanel gp) {
        super(gp);
        gp.getBoard().addTurnListener(this::updateApplicability);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Obstacle) {
            return ((Obstacle)o).getNameID().equals(getNameID());
        }
        return false;
    }

    @Override
    protected boolean determineApplicability() {
         return gp.getState() != GamePanel.ENDING;
    }
}
