package game.bonuses;

import game.GameModifier;
import game.GamePanel;

/**
 * Class that describes an abstract game bonus.
 *
 * @author Artem Novak
 */
public abstract class Bonus extends GameModifier {

    /**
     * Constructs an abstract bonus.
     *
     * @param gp GamePanel to interact with
     */
    public Bonus(GamePanel gp) {
        super(gp);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Bonus) {
            return ((Bonus)o).getNameID().equals(getNameID());
        }
        return false;
    }
}
