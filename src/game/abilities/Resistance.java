package game.abilities;

import game.GamePanel;
import game.events.AbilityListener;
import game.utils.WeightedRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that implements functionality of "resistance" passive ability.
 *
 * @author Artem Novak
 */
public class Resistance extends PassiveAbility {
    public static final int TRIGGER_PROBABILITY = 50;

    private final WeightedRandom random = new WeightedRandom();
    private final Map<Boolean, Integer> resultProbabilities = new HashMap<>();

    public Resistance(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager);
        resultProbabilities.put(true, TRIGGER_PROBABILITY);
        resultProbabilities.put(false, 100 - TRIGGER_PROBABILITY);
    }

    @Override
    public String getNameID() {
        return "resistance";
    }

    /**
     * Adds a {@link Resistance#TRIGGER_PROBABILITY} percent chance that incoming obstacle will get negated.
     * For further obstacle probability calculation in {@link game.obstacles.ObstacleManager} that obstacle is considered applied.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        gp.getObstacleManager().addObstacleListener(x -> {
            if (random.weightedChoice(resultProbabilities)) x.setObstacle(null);
        });
        for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
    }
}
