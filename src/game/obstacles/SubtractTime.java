package game.obstacles;

import game.GamePanel;

public class SubtractTime extends Obstacle {
    SubtractTime(GamePanel gp) {
        super(gp);
    }

    @Override
    public String getNameID() {
        return "subtractTime";
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
