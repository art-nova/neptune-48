package game;

import utils.GamePanelGraphics;
import utils.WeightedRandom;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Lightweight pair class for simplifying board position operations.
 *
 * @author Artem Novak
 */
class BoardCell {
    int row;
    int col;
    BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object o) {
        if (o instanceof BoardCell otherCell) {
            return otherCell.row == this.row && otherCell.col == this.col;
        }
        return false;
    }
}

/**
 * Class that implements logical and rendering functionality of the board.
 *
 * @author Artem Novak
 */
public class Board implements IRenderable {

    // Board states
    public static final int PLAYING = 0, SELECTING_TILE = 1, ANIMATING = 2;

    public int state = PLAYING;
    public int baseTileLevel;

    // Directions
    private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    private int moveDirection;

    private final GamePanel gp;
    private final GamePanelGraphics graphicsManager;
    private final WeightedRandom random = new WeightedRandom();
    private final Tile[][] board;
    private final List<Tile> transientTiles = new LinkedList<>(); // Tiles that are no longer logically present and are to be deleted after finishing current animation cycle.
    private final int rows;
    private final int cols;
    private int x, y;


    public Board(int rows, int cols, int baseTileLevel, GamePanel gp) throws IllegalArgumentException {
        if (rows < 2) throw new IllegalArgumentException("Cannot have less than 2 rows");
        if (cols < 2) throw new IllegalArgumentException("Cannot have less than 2 columns");
        if (baseTileLevel < 1 || baseTileLevel > 11)
            throw new IllegalArgumentException("Illegal base tile level: " + baseTileLevel);
        this.gp = gp;
        this.graphicsManager = gp.graphicsManager;
        this.rows = rows;
        this.cols = cols;
        board = new Tile[rows][cols];
        this.baseTileLevel = baseTileLevel;
    }

    public void update() {
        state = PLAYING;
        for (Tile[] row : board) {
            for (Tile tile : row) if (tile != null) tile.update();
        }
        for (Tile tile : transientTiles) {
            tile.update();
        }
        transientTiles.removeIf(x -> x.state == Tile.STATIC);

        if (state == PLAYING) {
            switch (gp.keyHandler.lastPressKey) {
                case "up" -> {
                    shiftUp();
                    gp.keyHandler.clearLastPress();
                    moveDirection = UP;
                }
                case "down" -> {
                    shiftDown();
                    gp.keyHandler.clearLastPress();
                    moveDirection = DOWN;
                }
                case "left" -> {
                    shiftLeft();
                    gp.keyHandler.clearLastPress();
                    moveDirection = LEFT;
                }
                case "right" -> {
                    shiftRight();
                    gp.keyHandler.clearLastPress();
                    moveDirection = RIGHT;
                }
            }
            if (state == ANIMATING) {
                generateRandomTile();
                gp.reactToTurn();
            }
        }
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(graphicsManager.getTexture("boardBG"), x, y, null);
        if (state == ANIMATING && moveDirection == UP) {
            for (Tile[] row : board) {
                for (Tile tile : row) if (tile != null) tile.render(g2d);
            }
        }
        else if (state == ANIMATING && moveDirection == DOWN) {
            for (int i = rows - 1; i >= 0; i--) {
                for (Tile tile : board[i]) if (tile != null) tile.render(g2d);
            }
        }
        else if (state == ANIMATING && moveDirection == LEFT) {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows; i++) if (board[i][j] != null) board[i][j].render(g2d);
            }
        }
        else {
            for (int j = cols - 1; j >= 0; j--) {
                for (int i = 0; i < rows; i++) if (board[i][j] != null) board[i][j].render(g2d);
            }
        }
        for (Tile tile : transientTiles) tile.render(g2d);
    }

    /**
     * Generates a random tile at a certain board position.
     * Reacts appropriately if the tile is unable to be generated (board full).
     */
    public void generateRandomTile() {
        ArrayList<BoardCell> freeBoardCells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null) freeBoardCells.add(new BoardCell(i, j));
            }
        }
        if (!freeBoardCells.isEmpty()) {
            BoardCell finalPoint = freeBoardCells.get(random.nextInt(0, freeBoardCells.size()));
            HashMap<Integer, Integer> newTileWeights = new HashMap<>();
            newTileWeights.put(baseTileLevel, 3);
            newTileWeights.put(baseTileLevel+1, 1);
            generateTile(finalPoint, random.<Integer>weightedChoice(newTileWeights));
        }
        else {
            if (checkForLoseCondition()) gp.loseLevel();
        }
    }

    /**
     * Generates a tile with specific settings (useful for any kind of scripted levels).
     *
     * @param cell tile's cell
     * @param level tile's level
     */
    public void generateTile(BoardCell cell, int level) {
        Point point = pointByCell(cell);
        board[cell.row][cell.col] = new Tile(point.x, point.y, level, gp);
    }

    Tile tileByBoardCell(BoardCell cell) {
        return board[cell.row][cell.col];
    }

    Point pointByCell(BoardCell cell) {
        return new Point(x + cell.col * GamePanelGraphics.TILE_SIZE + (cell.col + 1) * GamePanelGraphics.TILE_OFFSET,
                y + cell.row * GamePanelGraphics.TILE_SIZE + (cell.row + 1) * GamePanelGraphics.TILE_OFFSET);
    }

    private void shiftUp() {
        for (int j = 0; j < cols; j++) {
            BoardCell cellEdge = new BoardCell(0, j);
            for (int i = 1; i < rows; i++) {
                BoardCell cellDynamic = new BoardCell(i, j);
                if (board[i][j] != null && !board[i][j].locked) {
                    for (int z = i - 1; z >= 0; z--) {
                        if (cellAction(new BoardCell(z, j), cellDynamic, cellEdge, -1, 0)) break;
                    }
                }
            }
        }
    }

    private void shiftDown() {
        for (int j = 0; j < cols; j++) {
            BoardCell cellEdge = new BoardCell(rows - 1, j);
            for (int i = rows - 2; i >= 0; i--) {
                BoardCell cellDynamic = new BoardCell(i, j);
                if (board[i][j] != null && !board[i][j].locked) {
                    for (int z = i + 1; z < rows; z++) {
                        if (cellAction(new BoardCell(z, j), cellDynamic, cellEdge, 1, 0)) break;
                    }
                }
            }
        }
    }

    private void shiftLeft() {
        for (int i = 0; i < rows; i++) {
            BoardCell cellEdge = new BoardCell(i, 0);
            for (int j = 1; j < cols; j++) {
                BoardCell cellDynamic = new BoardCell(i, j);
                if (board[i][j] != null && !board[i][j].locked) {
                    for (int z = j - 1; z >= 0; z--) {
                        if (cellAction(new BoardCell(i, z), cellDynamic, cellEdge, 0, -1)) break;
                    }
                }
            }
        }
    }

    private void shiftRight() {
        for (int i = 0; i < rows; i++) {
            BoardCell cellEdge = new BoardCell(i, cols - 1);
            for (int j = cols - 2; j >= 0; j--) {
                BoardCell cellDynamic = new BoardCell(i, j);
                if (board[i][j] != null && !board[i][j].locked) {
                    for (int z = j + 1; z < cols; z++) {
                        if (cellAction(new BoardCell(i, z), cellDynamic, cellEdge, 0, 1)) break;
                    }
                }
            }
        }
    }

    /**
     * Handles turn interaction between two cells.
     *
     * @param cellStatic cell interaction with which is being assessed
     * @param cellDynamic cell whose action is being assessed
     * @param rowStep row shift per step of checking (outside this method)
     * @param colStep col shift per step of checking (outside this method)
     * @return true if the tiles interacted in any way (not necessarily moved), false otherwise
     */
    private boolean cellAction(BoardCell cellStatic, BoardCell cellDynamic, BoardCell cellEdge, int rowStep, int colStep) {
        Tile tileStatic = tileByBoardCell(cellStatic);
        Tile tileDynamic = tileByBoardCell(cellDynamic);
        // Move to edge case
        if (tileStatic == null && cellStatic.equals(cellEdge)) {
            tileDynamic.moveTowards(pointByCell(cellStatic));
            board[cellStatic.row][cellStatic.col] = tileDynamic;
            board[cellDynamic.row][cellDynamic.col] = null;
            return true;
        }
        if (tileStatic != null) {
            // Merge case
            if (tileStatic.state != Tile.MERGING && !tileStatic.locked && tileStatic.level == tileDynamic.level) {
                tileDynamic.moveTowards(pointByCell(cellStatic));
                tileStatic.makeMergeBase();
                transientTiles.add(tileDynamic);
                board[cellDynamic.row][cellDynamic.col] = null;
                return true;
            }
            // Move limited by other tile case
            else {
                cellStatic.row -= rowStep;
                cellStatic.col -= colStep;
                // Case where movement does not occur because of the static tile
                if (cellStatic.equals(cellDynamic)) return true;
                // Actual move case
                else if (board[cellStatic.row][cellStatic.col] == null) {
                    tileDynamic.moveTowards(pointByCell(cellStatic));
                    board[cellStatic.row][cellStatic.col] = tileDynamic;
                    board[cellDynamic.row][cellDynamic.col] = null;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finishes all tile animations and deletes transient tiles.
     */
    private void flush() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile != null) tile.flush();
            }
        }
        transientTiles.removeIf(x -> true);
    }

    /**
     * Checks whether the player has or does not have any possible turns left on a full board. In other contexts errors may occur!
     *
     * @return true if the player has no turns and false if they still do
     */
    private boolean checkForLoseCondition() {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols - 1; j++) {
                if (board[i][j].level == board[i][j + 1].level && !board[i][j].locked && !board[i][j + 1].locked ||
                        board[i][j].level == board[i + 1][j].level && !board[i][j].locked && !board[i + 1][j].locked) return false;
            }
        }
        return true;
    }

}
