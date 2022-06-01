package utils;

import java.util.Map;
import java.util.Random;

/**
 * Клас, що реалізує випадковий вибір об'єктів за відносною частотою на базі класу Random.
 *
 * @author Artem Novak
 */
public class WeightedRandom extends Random {
    /**
     * Випадково обирає об'єкт із мапи за його відносною частотою ("вагою").
     *
     * @param weightMap мапа об'єктів до їх відносних частот.
     * @return випадково обраний на основі відносних частот об'єкт, або null, якщо нема об'єктів із непорожніми частотами.
     */
    public <T> T weightedChoice (Map<T, Integer> weightMap) {
        for (T key : weightMap.keySet()) {
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
