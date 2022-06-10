package game;

import game.utils.GamePanelGraphics;
import game.utils.WeightedRandom;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Class that implements logical and rendering functionality of the board.
 *
 * @author Artem Novak
 */
public class Board extends GameObject {

    // Board states
    public static final int STATIC = 0, SELECTING = 1, ANIMATING = 2;

    public final int preferredWidth;
    public final int preferredHeight;

    public int state = STATIC;
    public int baseTileLevel;
    public final Tile[][] board;

    // Directions
    private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    private final WeightedRandom random = new WeightedRandom();
    private final List<Tile> transientTiles = new LinkedList<>(); // Tiles that are no longer logically present and are to be deleted after finishing current animation cycle.
    private final SelectionHandler selectionHandler;
    private final int rows;
    private final int cols;
    private int x, y;
    private int tileCount;

    private int moveDirection;
    private boolean turnReactionScheduled;

    public Board(int rows, int cols, int baseTileLevel, GamePanel gp) throws IllegalArgumentException {
        super(0, 0, gp);
        if (rows < 2) throw new IllegalArgumentException("Cannot have less than 2 rows");
        if (cols < 2) throw new IllegalArgumentException("Cannot have less than 2 columns");
        if (baseTileLevel < 1 || baseTileLevel > 11)
            throw new IllegalArgumentException("Illegal base tile level: " + baseTileLevel);
        this.gp = gp;
        this.rows = rows;
        this.cols = cols;
        this.baseTileLevel = baseTileLevel;
        this.selectionHandler = new SelectionHandler();
        board = new Tile[rows][cols];
        preferredWidth = GamePanelGraphics.TILE_SIZE * cols + GamePanelGraphics.TILE_OFFSET * (cols + 1);
        preferredHeight = GamePanelGraphics.TILE_SIZE * rows + GamePanelGraphics.TILE_OFFSET * (rows + 1);
    }

    /**
     * Inner class that oversees visual selection of board cells by the player.
     *
     * @author Artem Novak
     */
    private class SelectionHandler extends GameObject {
        public List<BoardCell> selected = new ArrayList<>();
        private Predicate<BoardCell> predicate;
        private int maxSelection;
        private final BufferedImage highlight;
        private BufferedImage overlay;

        /**
         * Prepares a new SelectionHandler instance (predicate and maxSelection have to be set later).
         */
        private SelectionHandler() {
            super(Board.this.x, Board.this.y, Board.this.gp);
            highlight = new BufferedImage(GamePanelGraphics.TILE_SIZE + GamePanelGraphics.TILE_OFFSET, GamePanelGraphics.TILE_SIZE + GamePanelGraphics.TILE_OFFSET, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) highlight.getGraphics();
            Area highlightArea = new Area(new Rectangle(GamePanelGraphics.TILE_SIZE + GamePanelGraphics.TILE_OFFSET, GamePanelGraphics.TILE_SIZE + GamePanelGraphics.TILE_OFFSET));
            highlightArea.subtract(new Area(new Rectangle( GamePanelGraphics.TILE_OFFSET/2,  + GamePanelGraphics.TILE_OFFSET/2, GamePanelGraphics.TILE_SIZE, GamePanelGraphics.TILE_SIZE)));
            g2d.setColor(gp.graphics.getColor("highlight"));
            g2d.fill(highlightArea);
            g2d.dispose();
        }

        /**
         * Generates a new dark overlay over non-selectable cells based on the selectability predicate.
         */
        public void resetOverlay() {
            overlay = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_ARGB);
            Area overlayArea = new Area(new Rectangle((int)x, (int)y, preferredWidth, preferredHeight));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    BoardCell cell = new BoardCell(i, j);
                    if (predicate.test(cell)) {
                        Point point = pointByCell(cell);
                        overlayArea.subtract(new Area(new Rectangle(point.x, point.y, GamePanelGraphics.TILE_SIZE, GamePanelGraphics.TILE_SIZE)));
                    }
                }
            }
            Graphics2D g2d = (Graphics2D) overlay.getGraphics();
            g2d.setColor(new Color(0, 0, 0, 128));
            g2d.fill(overlayArea);
            g2d.dispose();
        }

        /**
         * Checks whether any cell has been clicked on.
         * If it has and if it is selectable, changes it from selected to non-selected or vice versa.
         */
        public void update() {
            if (predicate == null) throw new GameLogicException("Updating a selector without a selection predicate");
            if (gp.mouseHandler.mouseOn && gp.mouseHandler.mousePressed) {
                gp.mouseHandler.mousePressed = false;
                BoardCell cell = cellByMouseLocation();
                if (cell != null && predicate.test(cell)) {
                    if (selected.contains(cell)) {
                        selected.remove(cell);
                    }
                    else if (selected.size() < maxSelection) {
                        selected.add(cell);
                    }
                }
            }
        }

        /**
         * Renders a dark overlay over unselectable part of the board, highlights around selected cells and the cell mouse is hovering over.
         *
         * @param g2d Graphics2D instance for rendering
         */
        public void render(Graphics2D g2d) {
            if (predicate == null) throw new GameLogicException("Rendering a selector without a selection predicate");
            g2d.drawImage(overlay, (int)x, (int)y, null);
            for (BoardCell cell : selected) highlightCell(cell, g2d);
            if (gp.mouseHandler.mouseOn) {
                BoardCell cell = cellByMouseLocation();
                if (cell != null && predicate.test(cell)) {
                    highlightCell(cell, g2d);
                }
            }
        }

        private BoardCell cellByMouseLocation() {
            Point mouseScreenLocation = MouseInfo.getPointerInfo().getLocation();
            Point gpScreenLocation = gp.getLocationOnScreen();
            Point mouseGameLocation = new Point((int)((mouseScreenLocation.x - gpScreenLocation.x)/gp.graphics.scale),
                    (int)((mouseScreenLocation.y - gpScreenLocation.y)/gp.graphics.scale));
            return cellByPoint(mouseGameLocation);

        }

        private void highlightCell(BoardCell cell, Graphics2D g2d) {
            Point cellPoint = pointByCell(cell);
            g2d.drawImage(highlight, cellPoint.x - GamePanelGraphics.TILE_OFFSET/2, cellPoint.y - GamePanelGraphics.TILE_OFFSET/2, null);
        }
    }

    public void update() {
        if (state == STATIC) {
            if (gp.keyHandler.lastPressKey != null) {
                handlePlayingKeyInput();
                if (turnReactionScheduled) {
                    turnReactionScheduled = false;
                    generateRandomTile();
                    gp.reactToTurn();
                }
            }
        }
        if (state == ANIMATING || state == STATIC) {
            state = STATIC; // Gets set to ANIMATING by updating tiles if any of them are being animated
            for (Tile[] row : board) {
                for (Tile tile : row) if (tile != null) tile.update();
            }
            for (Tile tile : transientTiles) {
                tile.update();
            }
            transientTiles.removeIf(x -> x.state == Tile.STATIC);
        }
        else if (state == SELECTING) {
            selectionHandler.update();
            if (gp.keyHandler.lastPressKey != null && gp.keyHandler.lastPressKey.equals("escape")) exitSelection();
        }
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(gp.graphics.getTexture("boardBG"), x, y, null);
        if (state != ANIMATING || moveDirection == UP) {
            for (Tile[] row : board) {
                for (Tile tile : row) if (tile != null) tile.render(g2d);
            }
        }
        else if (moveDirection == DOWN) {
            for (int i = rows - 1; i >= 0; i--) {
                for (Tile tile : board[i]) if (tile != null) tile.render(g2d);
            }
        }
        else if (moveDirection == LEFT) {
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
        if (state == SELECTING) selectionHandler.render(g2d);
    }

    /**
     * Generates a random tile at a certain board position.
     * Reacts appropriately if the tile is unable to be generated (board full).
     */
    public void generateRandomTile() throws GameLogicException {
        if (tileCount == rows * cols) throw new GameLogicException("Attempt to generate a tile on a full board");
        ArrayList<BoardCell> freeBoardCells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null) freeBoardCells.add(new BoardCell(i, j));
            }
        }
        if (!freeBoardCells.isEmpty()) {
            BoardCell finalCell = freeBoardCells.get(random.nextInt(0, freeBoardCells.size()));
            HashMap<Integer, Integer> newTileWeights = new HashMap<>();
            newTileWeights.put(baseTileLevel, 3);
            newTileWeights.put(baseTileLevel+1, 1);
            generateTile(finalCell, random.<Integer>weightedChoice(newTileWeights));
        }
    }

    /**
     * Generates a tile with specific settings (useful for any kind of scripted levels).
     *
     * @param cell tile's cell
     * @param level tile's level
     */
    public void generateTile(BoardCell cell, int level) throws GameLogicException {
        if (tileCount == rows * cols) throw new GameLogicException("Attempt to generate a tile on a full board");
        Point point = pointByCell(cell);
        board[cell.row][cell.col] = new Tile(point.x, point.y, level, gp);
        tileCount++;
        if (tileCount == rows * cols && checkForLoseCondition()) gp.loseLevel();
    }

    public Tile tileByBoardCell(BoardCell cell) {
        try {
            return board[cell.row][cell.col];
        }
        catch (IndexOutOfBoundsException ignore) {
            return null;
        }
    }

    /**
     * Initiates cell selection.
     *
     * @param predicate predicate which is used to define selectable cells.
     * @param maxSelection max possible number of selected cells
     */
    public void initSelection(Predicate<BoardCell> predicate, int maxSelection) {
        if (maxSelection > rows*cols) throw new GameLogicException("Specified max number of selectable cells is larger than overall number of cells");
        selectionHandler.predicate = predicate;
        selectionHandler.maxSelection = maxSelection;
        selectionHandler.resetOverlay();
        flush();
        state = SELECTING;
    }

    public void exitSelection() {
        selectionHandler.selected = new ArrayList<>();
        selectionHandler.predicate = null;
        state = STATIC;
    }

    public List<BoardCell> getSelectedCells() {
        return selectionHandler.selected;
    }

    /**
     * Changes the current selection predicate if the board is in the SELECTING state
     *
     * @param predicate new predicate
     */
    public void changeSelectionPredicate(Predicate<BoardCell> predicate) {
        if (state == SELECTING) {
            selectionHandler.predicate = predicate;
            selectionHandler.resetOverlay();
        }
    }

    /**
     * Finds origin point of a given board cell.
     *
     * @param cell board cell
     * @return base-scale point relative to the GamePanel
     */
    public Point pointByCell(BoardCell cell) {
        return new Point(x + cell.col * GamePanelGraphics.TILE_SIZE + (cell.col + 1) * GamePanelGraphics.TILE_OFFSET,
                y + cell.row * GamePanelGraphics.TILE_SIZE + (cell.row + 1) * GamePanelGraphics.TILE_OFFSET);
    }

    /**
     * Finds the board cell corresponding to given point
     *
     * @param point base-scale point relative to the GamePanel
     * @return board cell at the given point, or null if there is no board cell at the point
     */
    private BoardCell cellByPoint(Point point) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BoardCell cell = new BoardCell(i, j);
                Point cellPoint = pointByCell(cell);
                Rectangle bounds = new Rectangle(cellPoint.x, cellPoint.y, GamePanelGraphics.TILE_SIZE, GamePanelGraphics.TILE_SIZE);
                if (bounds.contains(point)) return cell;
            }
        }
        return null;
    }

    private void handlePlayingKeyInput() {
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
        }}

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
            turnReactionScheduled = true;
            return true;
        }
        if (tileStatic != null) {
            // Merge case
            if (tileStatic.state != Tile.MERGING && !tileStatic.locked && tileStatic.level == tileDynamic.level) {
                tileDynamic.moveTowards(pointByCell(cellStatic));
                tileStatic.makeMergeBase();
                transientTiles.add(tileDynamic);
                board[cellDynamic.row][cellDynamic.col] = null;
                tileCount--;
                turnReactionScheduled = true;
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
                    turnReactionScheduled = true;
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((j < cols - 1 && board[i][j].level == board[i][j + 1].level && !board[i][j].locked && !board[i][j + 1].locked) ||
                        (i < rows - 1 && board[i][j].level == board[i + 1][j].level && !board[i][j].locked && !board[i + 1][j].locked)) return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Tile[] row : board) {
            for (Tile tile : row) str.append(tile == null ? "-" : tile.level).append(" ");
            str.append("\n");
        }
        return str.toString();
    }

}
