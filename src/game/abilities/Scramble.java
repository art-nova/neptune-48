package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;
import game.events.StateListener;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Tile;

import java.util.*;

/**
 * Class that implements functionality of "scramble" active ability along with tile selection for it.
 *
 * @author Artem Novak
 */
public class Scramble extends ActiveAbility {
    public static final int DEFAULT_COOLDOWN = 10;

    private final Random random = new Random();

    public Scramble(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager, DEFAULT_COOLDOWN);
    }

    @Override
    public String getNameID() {
        return "scramble";
    }

    @Override
    public void startApplication() {
        super.startApplication();

        List<BoardCell> cells = board.getCellsByPredicate(x -> board.getTileInCell(x) != null);
        Map<Tile, BoardCell> tilesOrigins = new HashMap<>();
        for (BoardCell cell : cells) tilesOrigins.put(board.getTileInCell(cell), cell);
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

        if (board.getState() == Board.ANIMATING) {
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
        else {
            for (Tile tile : tilesDestinations.keySet()) board.animateTileMove(tile, tilesDestinations.get(tile));
            board.setState(Board.ANIMATING);
        }

        currentCooldown = cooldown;
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
