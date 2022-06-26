package game;

import game.events.UIDataListener;

/**
 * Interface that describes an object with information displayed by UI.
 * Therefore, such objects needs to possess UIDataListeners and notify them whenever its specific UI-related data is changed.
 *
 * @author Artem Novak
 */
public interface UIDataHolder {
    void addUIDataListener(UIDataListener listener);

    void removeUIDataListener(UIDataListener listener);
}
