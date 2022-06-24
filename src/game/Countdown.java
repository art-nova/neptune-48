package game;

import game.events.UIDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements turn-based countdown allowing for modifications.
 * As a {@link UIDataHolder} notifies its listeners on any turn change.
 *
 * @author Artem Novak
 */
public class Countdown implements UIDataHolder {
    public static final int IDLE = 0, TICKING = 1;

    private final List<UIDataListener> uiDataListeners = new ArrayList<>();

    private int dedicatedTurns;
    private int turns;
    private int state;

    /**
     * Constructs a countdown from given turn number to zero.
     *
     * @param turns number of turns dedicated for the level
     */
    public Countdown(int turns, GamePanel gp) {
        this.dedicatedTurns = turns;
        this.turns = turns;
        gp.getBoard().addTurnListener(() -> {
            if (state == TICKING) offsetTurns(-1);
        });
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
            for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
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
    }

    public void addUIDataListener(UIDataListener listener) {
        uiDataListeners.add(listener);
    }

    public void removeUIDataListener(UIDataListener listener) {
        uiDataListeners.remove(listener);
    }
}
