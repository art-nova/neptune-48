package game;

import java.util.LinkedList;

/**
 * Class that implements methods for scheduling actions to be processed in the game loop by certain NameIDs.
 *
 * @author Artem Novak
 */
public class ActionHandler {
    private LinkedList<String> scheduledActions = new LinkedList<>();

    /**
     * Determines whether there are any scheduled actions.
     *
     * @return true if there is at least one scheduled action
     */
    public boolean anyActionScheduled() {
        return !scheduledActions.isEmpty();
    }

    /**
     * Gets the most prioritized action's NameID.
     *
     * @return prioritized action (the earliest scheduled)
     */
    public String getPriorityAction() {
        return scheduledActions.peek();
    }

    /**
     * Checks whether an action is a priority action.
     *
     * @param nameID action's NameID
     * @return true if it is scheduled and is the head of the list (scheduled the earliest)
     */
    public boolean isPriorityAction(String nameID) {
        String priority = getPriorityAction();
        return priority != null && priority.equals(nameID);
    }

    /**
     * Schedules an action by NameID to be processed inside the game loop.
     *
     * @param nameID action's NameID
     */
    public void scheduleAction(String nameID) {
        scheduledActions.remove(nameID);
        scheduledActions.add(nameID);
    }

    /**
     * Clears one instance of a particular action from the list of actions to be processed by the game loop.
     *
     * @param nameID action's NameID
     */
    public void clearAction(String nameID) {
        scheduledActions.remove(nameID);
    }

    /**
     * Clears all scheduled actions.
     */
    public void clearAll() {
        scheduledActions = new LinkedList<>();
    }
}
