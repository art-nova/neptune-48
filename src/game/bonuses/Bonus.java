package game.bonuses;

import game.Board;
import game.GameModifier;
import game.GamePanel;

/**
 * Class that describes an abstract game bonus.
 *
 * @author Artem Novak
 */
public abstract class Bonus implements GameModifier {
    protected GamePanel gp;
    protected boolean applicable;
    protected boolean active;

    /**
     * Constructs an abstract bonus.
     *
     * @param gp GamePanel to interact with
     */
    public Bonus(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public boolean isApplicable() {
        return applicable && gp.getBoard().getState() == Board.STATIC;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Bonus) {
            return ((Bonus)o).getNameID().equals(getNameID());
        }
        return false;
    }
}
