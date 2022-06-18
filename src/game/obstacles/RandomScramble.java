package game.obstacles;

import game.GamePanel;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.*;

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
        board.addTurnListener(() -> {
        });
    }

    @Override
    public String getNameID() {
        return "randomScramble";
    }

    @Override
    public void startApplication() {
        Map<Tile, BoardCell> tilesOrigins = new HashMap<>();
        for (BoardCell cell : lastCheckCells) tilesOrigins.put(board.getTileInCell(cell), cell);
        List<BoardCell> destinationCells = board.getCellsByPredicate(x -> true);
        Map<Tile, BoardCell> tilesDestinations = new HashMap<>(); // Map for delayed animations.

        for (Tile tile : tilesOrigins.keySet()) {
            BoardCell originCell = tilesOrigins.get(tile);
            boolean originVacant = destinationCells.contains(originCell);

            destinationCells.remove(tilesOrigins.get(tile)); // Temporarily removing current tile cell from the pool to guarantee movement.
            BoardCell destination = destinationCells.get(random.nextInt(0, destinationCells.size()));
            board.putTileInCell(tile, destination);
            tilesDestinations.put(tile, destination);
            destinationCells.remove(destination);
            if (originVacant) destinationCells.add(tilesOrigins.get(tile)); // Returning cell the tile was in as a possible destination for other tiles.
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

    @Override
    protected boolean determineApplicability() {
        lastCheckCells = board.getCellsByPredicate(x -> board.getTileInCell(x) != null);
        return lastCheckCells.size() > 3;
    }
}
