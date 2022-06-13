package game.events;

import game.BoardCell;

import java.util.List;

/**
 * Interface that allows to register selection-related events.
 *
 * @author Artem Novak
 */
public interface CellSelectionListener {
    /**
     * Triggers when cells are successfully selected in Board selection mode.
     * That is, max amount of cells that fit selection predicate are selected.
     */
    void onSelectionCompleted(List<BoardCell> cells);

    /**
     * Triggers when selection was ended by any means other than completion (pressing Escape, starting another selection etc.).
     */
    void onSelectionAborted();
}
