package game.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * Class that implements random weighted choice from a collection of objects ()
 *
 * @author Artem Novak
 */
public class WeightedRandom extends Random {
    /**
     * Chooses a random key from the map, probability is based on map values (weights).
     *
     * @param weightMap map of objects to their generation weights
     * @return object chosen randomly based on weights, or null if no such object could be chosen
     */
    public <T> T weightedChoice (Map<T, Integer> weightMap) {
        for (T key : new HashSet<>(weightMap.keySet())) {
            if (weightMap.get(key) == null || weightMap.get(key) <= 0) weightMap.remove(key);
        }
        int num = nextInt(0, weightMap.values().stream().mapToInt(x -> x).sum());
        int upperBound = 0;
        for (T key : weightMap.keySet()) {
            upperBound += weightMap.get(key);
            if (num < upperBound) return key;
        }
        return null;
    }
}
