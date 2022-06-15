package game.obstacles;

import game.GamePanel;
import game.events.ObstacleEvent;
import game.events.ObstacleListener;
import game.utils.WeightedRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that handles application of in-game obstacles.
 *
 * @author Artem Novak
 */
public class ObstacleManager {
    private final int minInterval;
    private final int maxInterval;
    private final Map<Obstacle, Integer> obstacleWeights = new HashMap<>();
    private final Map<Boolean, Integer> triggerLikelihood = new HashMap<>();
    private final WeightedRandom random = new WeightedRandom();
    private final List<ObstacleListener> obstacleListeners = new ArrayList<>();
    private final GamePanel gp;

    private int turnsElapsed = 0;

    /**
     * Creates a new ObstacleManager with specified obstacles and their probability.
     *
     * @param obstacleWeights map of obstacle NameIDs to their weights (relative likelihood of occurrence when an abstract obstacle is triggered)
     * @param minInterval minimal interval in turns between two obstacles
     * @param maxInterval maximal interval in turns between two obstacles
     * @param gp {@link GamePanel}
     */
    public ObstacleManager(Map<String, Integer> obstacleWeights, int minInterval, int maxInterval, GamePanel gp) {
        this.gp = gp;
        for (String nameID : obstacleWeights.keySet()) {
            Integer weight = obstacleWeights.get(nameID);
            this.obstacleWeights.put(registerObstacle(nameID), weight);
        }
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        triggerLikelihood.put(true, 1);
        tryForObstacle();
        gp.getBoard().addTurnListener(() -> {
            turnsElapsed++;
            tryForObstacle();
        });
    }

    private Obstacle registerObstacle(String nameID) {
        switch (nameID) {
            case "downgrade" -> {
                System.out.println("downgrade"); // PLACEHOLDER
            }
            case "freeze" -> {
                System.out.println("freeze"); // PLACEHOLDER
            }
            case "garbageTile" -> {
                System.out.println("garbageTile"); // PLACEHOLDER
            }
            case "randomDispose" -> {
                System.out.println("randomDispose"); // PLACEHOLDER
            }
            case "randomScramble" -> {
                System.out.println("randomScramble"); // PLACEHOLDER
            }
            case "randomSwap" -> {
                System.out.println("randomSwap"); // PLACEHOLDER
            }
            case "subtractTime" -> {
                return new SubtractTime(gp);
            }
            default -> {
                throw new IllegalArgumentException("Obstacle " + nameID + " does not exist");
            }
        }
        return null;
    }

    private void tryForObstacle() {
        if (turnsElapsed >= minInterval) {
            triggerLikelihood.put(false, maxInterval + 1 - turnsElapsed);
            if (random.weightedChoice(triggerLikelihood)) {
                Obstacle obstacle;
                do {
                    obstacle = random.weightedChoice(this.obstacleWeights);
                    if (obstacle == null) break;
                } while (obstacle.getState() != Obstacle.APPLICABLE);
                ObstacleEvent e = new ObstacleEvent(obstacle);
                for (ObstacleListener listener : new ArrayList<>(obstacleListeners)) listener.onObstacle(e);
                if (e.getObstacle() != null) e.getObstacle().startApplication();
                turnsElapsed = 0;
            }
        }
    }

    public void addObstacleListener(ObstacleListener listener) {
        obstacleListeners.add(listener);
    }

    public void removeObstacleListener(ObstacleListener listener) {
        obstacleListeners.remove(listener);
    }
}
