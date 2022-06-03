package game.bonuses;

import game.GamePanel;

/**
 * Class that represents a time-adding bonus.
 *
 * @author Artem Novak
 */
public class BonusTime extends Bonus {
    BonusTime(GamePanel gp) {
        super(gp);
    }

    @Override
    public String getNameID() {
        return "bonusTime";
    }

    @Override
    public void apply() {
        //TODO
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
