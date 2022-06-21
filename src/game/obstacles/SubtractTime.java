package game.obstacles;

import game.Countdown;
import game.GamePanel;

/**
 * Class that implements functionality of "subtractTime" obstacle.
 *
 * @author Artem Novak
 */
public class SubtractTime extends Obstacle {
    /**
     * Number of seconds this bonus subtracts from the timer.
     */
    public static final int TIME_SUBTRACTED = 30;

    private Countdown countdown;

    public SubtractTime(GamePanel gp) {
        super(gp);
        countdown = gp.getCountdown();
    }

    @Override
    public String getNameID() {
        return "subtractTime";
    }

    @Override
    public void startApplication() {
        countdown.offsetTime(-TIME_SUBTRACTED);
    }

    @Override
    protected boolean determineApplicability() {
        return true;
    }
}
