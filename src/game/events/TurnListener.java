package game.events;

/**
 * Interface that allows to register turns on the board.
 *
 * @author Artem Novak
 */
public interface TurnListener {
    /**
     * Is triggered after a turn occurs.
     */
    void onTurn();
}
