package game.gameobjects;
/**
 * Lightweight pair class for simplifying board position operations.
 *
 * @author Artem Novak
 */
public class BoardCell {
    public int row;
    public int col;
    BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardCell otherCell) {
            return otherCell.row == this.row && otherCell.col == this.col;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[row = " + row + "; col = " + col + "]";
    }
}