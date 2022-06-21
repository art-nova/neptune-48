package game;

import game.events.UIDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements frame-based seconds countdown allowing for modifications.
 * As a {@link UIDataHolder} notifies its listeners on any time change.
 *
 * @author Artem Novak
 */
public class Countdown implements UIDataHolder {
    public static final int IDLE = 0, TICKING = 1;

    private final List<UIDataListener> uiDataListeners = new ArrayList<>();

    private int dedicatedTime;
    private int time;
    private int state;
    private int framesPassed;

    /**
     * Constructs a countdown from given time in seconds.
     *
     * @param time number of seconds this countdown lasts
     */
    public Countdown(int time) {
        this.dedicatedTime = time;
        this.time = time;
    }

    public void update() {
        if (state == TICKING) {
            framesPassed++;
            if (framesPassed >= 60) {
                offsetTime(-1);
                framesPassed = 0;
            }
        }
    }

    /**
     * Sets time from which following countdowns start (no effect on the currently proceeding countdown).
     *
     * @param time time in seconds
     */
    public void setDedicatedTime(int time) {
        this.dedicatedTime = time;
    }

    /**
     * Offsets time left by adding a given number of seconds.
     *
     * @param delta number of seconds added
     */
    public void offsetTime(int delta) {
        int oldTime = time;
        time = Math.max(time + delta, 0);
        if (time <= 0) state = IDLE;
        if (oldTime != time) {
            for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
        }
    }

    public int getDedicatedTime() {
        return dedicatedTime;
    }

    public int getTime() {
        return time;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        time = dedicatedTime;
        state = TICKING;
    }

    public void addUIDataListener(UIDataListener listener) {
        uiDataListeners.add(listener);
    }

    public void removeUIDataListener(UIDataListener listener) {
        uiDataListeners.remove(listener);
    }
}
