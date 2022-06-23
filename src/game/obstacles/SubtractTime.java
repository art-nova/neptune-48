package game.obstacles;

import game.Countdown;
import game.GamePanel;

/**
 * Class that implements functionality of "subtractTime" obstacle.
 *
 * @author Artem Novak
 */
public class SubtractTime extends Obstacle {
    public static final int TIME_SUBTRACTED_PERCENTAGE = 5;

    private final Countdown countdown;
    private final int timeSubtracted;

    public SubtractTime(GamePanel gp) {
        super(gp);
        countdown = gp.getCountdown();
        timeSubtracted = Math.round(gp.getCountdown().getDedicatedTime() * TIME_SUBTRACTED_PERCENTAGE / 100f);
    }

    @Override
    public String getNameID() {
        return "subtractTime";
    }

    @Override
    public void startApplication() {
        countdown.offsetTime(-timeSubtracted);
    }

    @Override
    protected boolean determineApplicability() {
        return true;
    }
}
