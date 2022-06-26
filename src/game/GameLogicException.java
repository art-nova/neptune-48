package game;

/**
 * Class-marker of game logic exceptions.
 *
 * @author Artem Novak
 */
public class GameLogicException extends RuntimeException {
    public GameLogicException() {
        super("Unspecified problem with game logic");
    }

    public GameLogicException(String msg) {
        super("Problem with game logic: " + msg);
    }
}
