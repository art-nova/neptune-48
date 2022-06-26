package game.events;

import game.gameobjects.BoardCell;

import java.util.List;

/**
 * Interface that allows to register selection-related events.
 *
 * @author Artem Novak
 */
public interface CellSelectionListener {
    /**
     * Triggers when a cell is added to or removed from selection.
     *
     * @param cells updated cell selection
     */
    void onSelectionUpdated(List<BoardCell> cells);

    void onSelectionCompleted(List<BoardCell> cells);

    /**
     * Triggers when selection was ended by any means other than completion (pressing Escape, starting another selection etc.).
     */
    void onSelectionAborted();
}
