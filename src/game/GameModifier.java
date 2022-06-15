package game;

import game.events.UIDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents an abstract game modifier - an entity that is not displayed, but influences the game process in some way.
 * <br>
 * As a {@link UIDataHolder}, triggers corresponding event when modifier state is changed.
 *
 * @author Artem Novak
 */
public abstract class GameModifier implements UIDataHolder {
    // States
    public static final int APPLICABLE = 0, UNAPPLICABLE = 1, APPLYING = 2;

    protected GamePanel gp;
    protected List<UIDataListener> uiDataListeners = new ArrayList<>();
    protected int state;

    public GameModifier(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * @return NameID of the modifier
     */
    public abstract String getNameID();

    /**
     * Starts application of this modifier. Depending on the modifier, it may or may not produce effect immediately.
     */
    public abstract void startApplication();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0 || state > 2) throw new IllegalArgumentException(getClass().getName() + " does not support state " + state);
        if (this.state != state) {
            this.state = state;
            for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
        }
    }

    public void addUIDataListener(UIDataListener listener) {
        uiDataListeners.add(listener);
    }

    public void removeUIDataListener(UIDataListener listener) {
        uiDataListeners.remove(listener);
    }
}
