package game.gameobjects;

import game.GameLogicException;
import game.GamePanel;
import game.KeyHandler;
import game.MouseHandler;
import game.events.CellSelectionListener;
import game.events.StateListener;
import game.events.TurnListener;
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
    public static final int IDLE = 0, ANIMATING = 1, SELECTING = 2;

    // Directions
    private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    private final WeightedRandom random = new WeightedRandom();
    private final List<Tile> transientTiles = new LinkedList<>(); // Tiles that are no longer logically present and are to be deleted after finishing current animation cycle.
    private final SelectionHandler selectionHandler;
    private final List<TurnListener> turnListeners = new ArrayList<>();
    private final List<StateListener> stateListeners = new ArrayList<>();
    private final List<CellSelectionListener> cellSelectionListeners = new ArrayList<>();
    private final int rows;
    private final int cols;
    private final int preferredWidth;
    private final int preferredHeight;
    private final GamePanelGraphics graphics;
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;

    private int state = IDLE;
    private int baseTileLevel;
    private final Tile[][] board;
    private int tileCount;
    private int moveDirection;
    private boolean turnReactionScheduled;
    private boolean locked = false;

    public Board(int x, int y, int rows, int cols, int baseTileLevel, GamePanel gp) throws IllegalArgumentException {
        super(x, y, gp);
        if (rows < 2) throw new IllegalArgumentException("Cannot have less than 2 rows");
        if (cols < 2) throw new IllegalArgumentException("Cannot have less than 2 columns");
        if (baseTileLevel < 1 || baseTileLevel > 11)
            throw new IllegalArgumentException("Illegal base tile level: " + baseTileLevel);
        this.gp = gp;
        this.graphics = gp.getGameGraphics();
        this.keyHandler = gp.getKeyHandler();
        this.mouseHandler = gp.getMouseHandler();
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
    private class SelectionHandler {
        public List<BoardCell> selected = new ArrayList<>();
        private Predicate<BoardCell> predicate;
        private int maxSelection;
        private final BufferedImage highlight;
        private BufferedImage overlay;

        /**
         * Prepares a new SelectionHandler instance (predicate and maxSelection have to be set later).
         */
        private SelectionHandler() {
            highlight = new BufferedImage(GamePanelGraphics.TILE_SIZE, GamePanelGraphics.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) highlight.getGraphics();
            g2d.setColor(graphics.getColor("highlight"));
            g2d.fillRect(0, 0, GamePanelGraphics.TILE_SIZE, GamePanelGraphics.TILE_SIZE);
            g2d.dispose();
        }

        /**
         * Generates a new dark overlay over non-selectable cells based on the selectability predicate.
         */
        public void resetOverlay() {
            overlay = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_ARGB);
            Area overlayArea = new Area(new Rectangle(0, 0, preferredWidth, preferredHeight));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    BoardCell cell = new BoardCell(i, j);
                    if (predicate.test(cell)) {
                        Point point = pointByCell(cell);
                        point.x -= Board.this.x;
                        point.y -= Board.this.y;
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
            if (mouseHandler.isMouseOn() && mouseHandler.isMousePressed()) {
                mouseHandler.resetMousePressed();
                BoardCell cell = cellByMouseLocation();
                if (cell != null && predicate.test(cell)) {
                    if (selected.contains(cell)) {
                        selected.remove(cell);
                    }
                    else {
                        selected.add(cell);
                        if (selected.size() == maxSelection) {
                            for (CellSelectionListener listener : new ArrayList<>(cellSelectionListeners)) listener.onSelectionCompleted(selected);
                            selectionHandler.selected = new ArrayList<>();
                            selectionHandler.predicate = null;
                            setState(IDLE);
                        }
                    }
                }
            }
            if (keyHandler.isKeyPressed() && keyHandler.getLastPressKey().equals("escape")) abortSelection();
        }

        /**
         * Renders a dark overlay over unselectable part of the board, highlights around selected cells and the cell mouse is hovering over.
         *
         * @param g2d Graphics2D instance for rendering
         */
        public void render(Graphics2D g2d) {
            if (predicate == null) throw new GameLogicException("Rendering a selector without a selection predicate");
            g2d.drawImage(overlay, (int)Board.this.x, (int)Board.this.y, null);
            for (BoardCell cell : selected) highlightCell(cell, g2d);
            if (mouseHandler.isMouseOn()) {
                BoardCell cell = cellByMouseLocation();
                if (cell != null && predicate.test(cell)) {
                    highlightCell(cell, g2d);
                }
            }
        }

        private BoardCell cellByMouseLocation() {
            Point mouseScreenLocation = MouseInfo.getPointerInfo().getLocation();
            Point gpScreenLocation = gp.getLocationOnScreen();
            Point mouseGameLocation = new Point((int)((mouseScreenLocation.x - gpScreenLocation.x)/graphics.scale),
                    (int)((mouseScreenLocation.y - gpScreenLocation.y)/graphics.scale));
            return cellByPoint(mouseGameLocation);

        }

        private void highlightCell(BoardCell cell, Graphics2D g2d) {
            Point cellPoint = pointByCell(cell);
            g2d.drawImage(highlight, cellPoint.x, cellPoint.y, null);
        }
    }

    public void update() {
        if (state == IDLE || state == ANIMATING) {
            if (!locked) handleTurnInput();
            updateTiles(); // May change board state.
        }
        else if (state == SELECTING) {
            selectionHandler.update();
        }
        else updateTiles();
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(graphics.getTexture("boardBG"), (int)x, (int)y, null);
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
     * Initiates cell selection (aborts currently running selection if there is one).
     *
     * @param predicate predicate which is used to define selectable cells.
     * @param maxSelection max possible number of selected cells
     */
    public void initSelection(Predicate<BoardCell> predicate, int maxSelection) {
        if (maxSelection > rows*cols) throw new GameLogicException("Specified max number of selectable cells is larger than overall number of cells");
        abortSelection();
        selectionHandler.predicate = predicate;
        selectionHandler.maxSelection = maxSelection;
        selectionHandler.resetOverlay();
        flush();
        setState(SELECTING);
    }

    /**
     * If a selection is ongoing, forcefully ends it and triggers listeners.
     */
    public void abortSelection() {
        if (state == SELECTING) {
            selectionHandler.selected = new ArrayList<>();
            selectionHandler.predicate = null;
            setState(IDLE);
            for (CellSelectionListener listener : new ArrayList<>(cellSelectionListeners)) listener.onSelectionAborted();
        }
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Tile[] row : board) {
            for (Tile tile : row) str.append(tile == null ? "-" : tile.getLevel()).append(" ");
            str.append("\n");
        }
        return str.toString();
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0 || state > 3) throw new IllegalArgumentException("Board does not support state " + state);
        if (state != this.state) {
            int oldState = this.state;
            this.state = state;
            for (StateListener listener : new ArrayList<>(stateListeners)) listener.onStateChanged(oldState, state);
        }
    }

    public int getBaseTileLevel() {
        return baseTileLevel;
    }

    public void setBaseTileLevel(int baseTileLevel) {
        if (baseTileLevel < 1 || baseTileLevel > 10) throw new GameLogicException("Trying to set illogical or impossible base tile level " + baseTileLevel);
        this.baseTileLevel = baseTileLevel;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Adds a turn listener.
     *
     * @param listener turn listener
     */
    public void addTurnListener(TurnListener listener) {
        turnListeners.add(listener);
    }

    /**
     * Removes a turn listener.
     *
     * @param listener turn listener
     */
    public void removeTurnListener(TurnListener listener) {
        turnListeners.remove(listener);
    }

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public void addCellSelectionListener(CellSelectionListener listener) {
        cellSelectionListeners.add(listener);
    }

    public void removeCellSelectionListener(CellSelectionListener listener) {
        cellSelectionListeners.remove(listener);
    }

    /**
     * Finds origin point of a given board cell.
     *
     * @param cell board cell
     * @return base-scale point relative to the GamePanel
     * @throws IllegalArgumentException if cell is outside of board bounds
     */
    public Point pointByCell(BoardCell cell) throws IllegalArgumentException {
        if (cell.row < 0 || cell.row >= rows || cell.col < 0 || cell.col > cols) throw new IllegalArgumentException("Nonexistent cell!");
        return new Point((int)x + cell.col * GamePanelGraphics.TILE_SIZE + (cell.col + 1) * GamePanelGraphics.TILE_OFFSET,
                (int)y + cell.row * GamePanelGraphics.TILE_SIZE + (cell.row + 1) * GamePanelGraphics.TILE_OFFSET);
    }

    /**
     * Generates a random tile at a certain board position.
     * Reacts appropriately if the tile is unable to be generated (board full).
     */
    public void generateRandomTile() throws GameLogicException {
        if (tileCount == rows * cols) throw new GameLogicException("Attempt to generate a tile on a full board");
        List<BoardCell> cells = getCellsByPredicate(x -> getTileInCell(x) == null);
        if (!cells.isEmpty()) {
            BoardCell cell = cells.get(random.nextInt(0, cells.size()));
            HashMap<Integer, Integer> newTileWeights = new HashMap<>();
            newTileWeights.put(baseTileLevel, 3);
            newTileWeights.put(baseTileLevel+1, 1);
            generateTile(cell, random.<Integer>weightedChoice(newTileWeights));
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
        Tile tile = new Tile(point.x, point.y, level, gp);
        board[cell.row][cell.col] = tile;
        tileCount++;
        if (tileCount == rows * cols && checkForLoseCondition()) gp.loseLevel();
    }

    /**
     * Logically deletes tile from the board.
     *
     * @param cell cell where the tile is located
     * @throws GameLogicException if the cell is nonexistent or empty
     */
    public void disposeCellContent(BoardCell cell) throws GameLogicException {
        Tile tile = getTileInCell(cell);
        if (tile == null) throw new GameLogicException("Trying to dispose of content of an empty cell");
        putTileInCell(null, cell);
        tileCount--;
    }

    /**
     * Animates disposing of a tile.
     *
     * @param tile tile whose disposal is to be animated
     * @throws GameLogicException if the tile is equal to null
     */
    public void animateTileDisposal(Tile tile) throws GameLogicException {
        flush();
        if (tile == null) throw new GameLogicException("Trying to dispose of a nonexistent tile");
        transientTiles.add(tile);
        tile.dispose();
    }

    /**
     * Adds tile to transient tiles and makes it keep displaying, even after its current animation finishes.
     *
     * @param tile tile
     */
    public void lingerTile(Tile tile) {
        transientTiles.add(tile);
        tile.setLingering(true);
    }

    /**
     * Removes tile from transient list.
     *
     * @param tile tile
     */
    public void unlingerTile(Tile tile) {
        transientTiles.remove(tile);
        tile.setLingering(false);
    }

    /**
     * Logically moves tile inside the given cell to the other, target cell.
     * Origin cell must contain a tile, target cell does not have limitations. This means that contents of the target cell will be overridden.
     *
     * @param originCell origin cell
     * @param targetCell target cell
     * @throws GameLogicException if there is no tile at origin cell
     */
    public void moveCellContent(BoardCell originCell, BoardCell targetCell) throws GameLogicException {
        Tile tile = getTileInCell(originCell);
        if (tile == null) throw new GameLogicException("Trying to move contents of an empty cell " + originCell);
        putTileInCell(null, originCell);
        putTileInCell(tile, targetCell);
    }

    /**
     * Initiates animation of tile moving to target cell.
     *
     * @param tile tile
     * @param targetCell target cell
     */
    public void animateTileMove(Tile tile, BoardCell targetCell) {
        flush();
        tile.moveTowards(pointByCell(targetCell));
    }

    /**
     * Initiates transient animation of movement of the given tile to a given point.
     * This animation is purely visual and separate from logical rendering of the tile, on which it has no effect.
     *
     * @param tile tile whose movement is to be animated
     * @param target target point
     * @throws GameLogicException if tile is null
     */
    public void animateTileMoveTransient(Tile tile, Point target) throws GameLogicException {
        flush();
        if (tile == null) throw new GameLogicException("Trying to move nonexistent tile");
        transientTiles.add(tile);
        tile.moveTowards(target);
    }

    /**
     * Logically swaps contents of two non-empty cells.
     *
     * @param cell1 first cell
     * @param cell2 second cell
     * @throws GameLogicException if either of the cells is empty
     */
    public void swapCellContents(BoardCell cell1, BoardCell cell2) throws GameLogicException {
        Tile tile1 = getTileInCell(cell1);
        Tile tile2 = getTileInCell(cell2);
        if (tile1 == null) throw new GameLogicException("Trying to initiate swapping while the first cell, " + cell1 +", is empty");
        if (tile2 == null) throw new GameLogicException("Trying to initiate swapping while the second cell, " + cell2 +", is empty");
        putTileInCell(tile1, cell2);
        putTileInCell(tile2, cell1);
    }

    /**
     * Logically merges contents of two cells and starts merge animation. Resulting tile is located at the merge base's cell.
     *
     * @param mergeBaseCell merge base cell
     * @param transientTileCell cell of the tile that will get deleted
     * @throws GameLogicException if either of the cells is empty, contains a 0-level tile or if their levels are not equal
     */
    public void fullMerge(BoardCell mergeBaseCell, BoardCell transientTileCell) throws GameLogicException {
        flush();
        Tile mergeBase = getTileInCell(mergeBaseCell);
        Tile transientTile = getTileInCell(transientTileCell);
        if (mergeBase == null) throw new GameLogicException("Trying to initiate merge while merge base cell, " + mergeBaseCell +", is empty");
        if (transientTile == null) throw new GameLogicException("Trying to initiate merge while transient tile cell, " + transientTileCell +", is empty");
        if (mergeBase.getLevel() == 0) throw new GameLogicException("Trying to merge a tile into a 0-level tile at cell " + mergeBaseCell);
        if (transientTile.getLevel() == 0) throw new GameLogicException("Trying to merge a 0-level tile from cell " + transientTileCell + " into merge base");
        if (mergeBase.getLevel() != transientTile.getLevel()) throw new GameLogicException("Trying to merge tiles of different levels, " + mergeBase.getLevel() + " and " + transientTile.getLevel());
        mergeBase.makeMergeBase();
        putTileInCell(null, transientTileCell);
        animateTileMoveTransient(transientTile, pointByCell(mergeBaseCell));
        tileCount--;
    }

    /**
     * Puts a tile into the given cell, overriding its previous content
     *
     * @param tile tile to be put
     * @param cell cell to put the tile in
     * @throws GameLogicException if there is no such cell within the boundaries of the board
     */
    public void putTileInCell(Tile tile, BoardCell cell) throws GameLogicException {
        try {
            board[cell.row][cell.col] = tile;
        }
        catch (IndexOutOfBoundsException e){
            throw new GameLogicException("Trying to put a tile into a nonexistent cell " + cell);
        }
    }

    /**
     * Gets cell's contents.
     *
     * @param cell cell to query from
     * @return {@link Tile} contained inside the cell, or null if cell is empty
     * @throws GameLogicException if there is no such cell within the boundaries of the board
     */
    public Tile getTileInCell(BoardCell cell) throws GameLogicException {
        try {
            return board[cell.row][cell.col];
        }
        catch (IndexOutOfBoundsException ignore) {
            throw new GameLogicException("Trying to get a tile from a nonexistent cell " + cell);
        }
    }

    /**
     * Gets a {@link List} of cells that satisfy the {@link Predicate}.
     *
     * @param predicate predicate
     * @return list of cells that satisfy the predicate
     */
    public List<BoardCell> getCellsByPredicate(Predicate<BoardCell> predicate) {
        List<BoardCell> cells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BoardCell cell = new BoardCell(i, j);
                if (predicate.test(cell)) cells.add(cell);
            }
        }
        return cells;
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

    private void handleTurnInput() {
        if (keyHandler.isKeyPressed()) {
            switch (keyHandler.getLastPressKey()) {
                case "up" -> {
                    shiftUp();
                    keyHandler.clearLastPress();
                    moveDirection = UP;
                    moveInputFollowup();
                }
                case "down" -> {
                    shiftDown();
                    keyHandler.clearLastPress();
                    moveDirection = DOWN;
                    moveInputFollowup();
                }
                case "left" -> {
                    shiftLeft();
                    keyHandler.clearLastPress();
                    moveDirection = LEFT;
                    moveInputFollowup();
                }
                case "right" -> {
                    shiftRight();
                    keyHandler.clearLastPress();
                    moveDirection = RIGHT;
                    moveInputFollowup();
                }
            }
        }

    }

    /**
     * If there is a turn reaction scheduled, reacts to it by notifying all listeners (normally happens after the animation).
     */
    private void moveInputFollowup() {
        if (turnReactionScheduled) {
            turnReactionScheduled = false;
            generateRandomTile();
            for (TurnListener listener : new ArrayList<>(turnListeners)) listener.onTurn();
        }
    }

    private void shiftUp() {
        for (int j = 0; j < cols; j++) {
            BoardCell cellEdge = new BoardCell(0, j);
            for (int i = 1; i < rows; i++) {
                BoardCell cellDynamic = new BoardCell(i, j);
                if (board[i][j] != null && !board[i][j].isLocked()) {
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
                if (board[i][j] != null && !board[i][j].isLocked()) {
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
                if (board[i][j] != null && !board[i][j].isLocked()) {
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
                if (board[i][j] != null && !board[i][j].isLocked()) {
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
        Tile tileStatic = getTileInCell(cellStatic);
        Tile tileDynamic = getTileInCell(cellDynamic);
        // Move to edge case
        if (tileStatic == null && cellStatic.equals(cellEdge)) {
            moveCellContent(cellDynamic, cellStatic);
            animateTileMove(tileDynamic, cellStatic);
            turnReactionScheduled = true;
            return true;
        }
        if (tileStatic != null) {
            // Merge case
            if (tileStatic.getState() != Tile.MERGING && !tileStatic.isLocked() && tileDynamic.getLevel() > 0 && tileStatic.getLevel() == tileDynamic.getLevel()) {
                fullMerge(cellStatic, cellDynamic);
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
                    moveCellContent(cellDynamic, cellStatic);
                    animateTileMove(tileDynamic, cellStatic);
                    turnReactionScheduled = true;
                    return true;
                }
            }
        }
        return false;
    }

    private void updateTiles() {
        boolean animating = false;
        for (Tile[] row : board) {
            for (Tile tile : row) if (tile != null) {
                tile.update();
                animating = animating || tile.getState() != Tile.IDLE;
            }
        }
        for (Tile tile : transientTiles) {
            tile.update();
            animating = animating || tile.getState() != Tile.IDLE;
        }
        transientTiles.removeIf(x -> x.getState() == Tile.IDLE && !x.isLingering());
        if (state == IDLE && animating) setState(ANIMATING);
        else if (state == ANIMATING && !animating) setState(IDLE);
    }

    /**
     * Finishes all tile animations and deletes transient tiles if animating.
     */
    public void flush() {
        if (state == ANIMATING) {
            for (Tile[] row : board) {
                for (Tile tile : row) {
                    if (tile != null) tile.flush();
                }
            }
            transientTiles.removeIf(x -> true);
            setState(IDLE);
        }
    }

    /**
     * Checks whether the player has or does not have any possible turns left on a full board. In other contexts errors may occur!
     *
     * @return true if the player has no turns and false if they still do
     */
    private boolean checkForLoseCondition() {
        if (tileCount == rows * cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if ((j < cols - 1 && board[i][j].getLevel() == board[i][j + 1].getLevel() && !board[i][j].isLocked() && !board[i][j + 1].isLocked()) ||
                            (i < rows - 1 && board[i][j].getLevel() == board[i + 1][j].getLevel() && !board[i][j].isLocked() && !board[i + 1][j].isLocked())) return false;
                }
            }
            return true;
        }
        return false;
    }

}
