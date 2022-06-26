package game.obstacles;

import game.GamePanel;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.*;
import java.util.function.Predicate;

/**
 * Class that implements functionality of "randomScramble" obstacle.
 *
 * @author Artem Novak
 */
public class RandomScramble extends Obstacle {
    private final Board board;
    private final Random random = new Random();

    private List<BoardCell> lastCheckCells = new ArrayList<>();

    public RandomScramble(GamePanel gp) {
        super(gp);
        this.board = gp.getBoard();
    }

    @Override
    public String getNameID() {
        return "randomScramble";
    }

    @Override
    public void startApplication() {
        Map<Tile, BoardCell> tilesOrigins = new HashMap<>();
        for (BoardCell cell : lastCheckCells) tilesOrigins.put(board.getTileInCell(cell), cell);
        List<BoardCell> destinationCells = board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile == null || !tile.isLocked();
        });
        Map<Tile, BoardCell> tilesDestinations = new HashMap<>(tilesOrigins);

        for (Tile tile : tilesOrigins.keySet()) {
            BoardCell currentDestination = tilesDestinations.get(tile);
            BoardCell excludedCell = tilesOrigins.get(tile); // Origin cell, excluded for obvious reasons.
            BoardCell excludedSwapCell = tilesDestinations.get(board.getTileInCell(currentDestination)); // Cell in which the tile with origin at current tile's location is located. May overlap with excludedCell.

            destinationCells.remove(excludedCell);
            destinationCells.remove(excludedSwapCell);

            BoardCell destination = destinationCells.get(random.nextInt(0, destinationCells.size()));
            // If destination is not empty, start swapping destinations between tiles.
            if (tilesDestinations.containsValue(destination)) {
                Tile otherTile = keyOfInterest(tilesDestinations, x -> tilesDestinations.get(x).equals(destination));
                tilesDestinations.put(otherTile, currentDestination);
            }
            tilesDestinations.put(tile, destination);
            destinationCells.add(excludedCell);
            if (!destinationCells.contains(excludedSwapCell)) destinationCells.add(excludedSwapCell);
        }

        for (Tile tile : tilesOrigins.keySet()) {
            BoardCell destination = tilesDestinations.get(tile);
            board.putTileInCell(tile, destination);
            destinationCells.remove(destination);
        }
        for (BoardCell leftover : destinationCells) board.putTileInCell(null, leftover);

        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    board.removeStateListener(this);
                    for (Tile tile : tilesDestinations.keySet()) board.animateTileMove(tile, tilesDestinations.get(tile));
                    board.setState(Board.ANIMATING);
                }
            }
        });
    }

    private <K, V> K keyOfInterest(Map<K, V> map, Predicate<K> predicate) {
        return map.keySet().stream().filter(predicate).findFirst().orElse(null);
    }

    @Override
    protected boolean determineApplicability() {
        lastCheckCells = board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && !tile.isLocked();
        });
        return lastCheckCells.size() > 3;
    }
}
