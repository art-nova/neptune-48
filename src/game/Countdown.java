package game;

import UI.LevelMenu;

/**
 * Implements turn-based countdown allowing for modifications.
 *
 * @author Artem Novak
 */
public class Countdown {
    public static final int IDLE = 0, TICKING = 1;

    private final GamePanel gp;
    private final LevelMenu ui;

    private int dedicatedTurns;
    private int turns;
    private int state;

    /**
     * Constructs a countdown from given turn number to zero.
     *
     * @param turns number of turns dedicated for the level
     * @param gp base GamePanel
     */
    public Countdown(int turns, GamePanel gp) {
        this.dedicatedTurns = turns;
        this.turns = turns;
        this.gp = gp;
        this.ui = gp.getBase();
    }

    /**
     * Sets turns from which following countdowns start (no effect on the currently proceeding countdown).
     *
     * @param turns number of turns
     */
    public void setDedicatedTime(int turns) {
        this.dedicatedTurns = turns;
    }

    /**
     * Offsets turns left by adding a given number.
     *
     * @param delta number of turns added
     */
    public void offsetTurns(int delta) {
        int oldTurns = turns;
        turns = Math.max(turns + delta, 0);
        if (turns <= 0) state = IDLE;
        if (oldTurns != turns) {
            ui.setTurnsLeft(turns);
        }
    }

    public int getDedicatedTurns() {
        return dedicatedTurns;
    }

    public int getTurns() {
        return turns;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        state = TICKING;
        gp.getBoard().addTurnListener(() -> {
            if (state == TICKING) offsetTurns(-1);
            if (turns <= 0 && gp.getState() != GamePanel.ENDING) gp.loseLevel();
        });
    }
}
