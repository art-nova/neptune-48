package game.obstacles;

import game.Countdown;
import game.GamePanel;

/**
 * Class that implements functionality of "subtractTime" obstacle.
 *
 * @author Artem Novak
 */
public class SubtractTurns extends Obstacle {
    public static final int SUBTRACTED_PERCENTAGE = 5;

    private final Countdown countdown;
    private final int turnsSubtracted;

    public SubtractTurns(GamePanel gp) {
        super(gp);
        countdown = gp.getCountdown();
        turnsSubtracted = Math.round(gp.getCountdown().getDedicatedTurns() * SUBTRACTED_PERCENTAGE / 100f);
    }

    @Override
    public String getNameID() {
        return "subtractTime";
    }

    @Override
    public void startApplication() {
        countdown.offsetTurns(-turnsSubtracted);
    }

    @Override
    protected boolean determineApplicability() {
        return true;
    }
}
