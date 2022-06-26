package game.events;

/**
 * Interface that allows to register updates of info that is to be displayed via UI.
 *
 * @author Artem Novak
 */
public interface UIDataListener {
    /**
     * Is triggered when any of the information that should display via UI changes.
     * Note: due to abstract and arbitrary nature of the event it does not supply any information on what exactly changed.
     */
    void onUIDataChanged();
}
