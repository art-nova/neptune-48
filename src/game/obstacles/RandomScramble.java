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
        List<Tile> tiles = new ArrayList<>();
        for (BoardCell cell : lastCheckCells) tiles.add(board.getTileInCell(cell));
        List<BoardCell> destinationCells = new ArrayList<>();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) destinationCells.add(new BoardCell(i, j));
        }
        Map<Tile, BoardCell> tilesDestinations = new HashMap<>();
        for (int i = 0; i < lastCheckCells.size(); i++) {
            int tileIndex = random.nextInt(0, tiles.size());
            int destinationIndex = random.nextInt(0, destinationCells.size());
            Tile tile = tiles.get(tileIndex);
            BoardCell destination = destinationCells.get(destinationIndex);
            tiles.remove(tileIndex);
            tilesDestinations.put(tile, destination);
            destinationCells.remove(destinationIndex);
            board.putTileInCell(tile, destination);
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
