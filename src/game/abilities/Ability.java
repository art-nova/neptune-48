package game.abilities;

import game.GameModifier;
import game.GamePanel;
import game.events.AbilityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that describes an abstract player-based useful ability (as opposed to
 * harmful implementations of {@link game.obstacles.Obstacle} coming from outside)
 *
 * @author Artem Novak
 */
public abstract class Ability extends GameModifier {
    protected final List<AbilityListener> abilityListeners = new ArrayList<>();
    protected final AbilityManager abilityManager;

    public Ability(GamePanel gp, AbilityManager abilityManager) {
        super(gp);
        this.abilityManager = abilityManager;
    }

    @Override
    public void setState(int state) {
        if (this.state != state) {
            super.setState(state);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Ability) return ((Ability) o).getNameID().equals(getNameID());
        return false;
    }

    public void addAbilityListener(AbilityListener listener) {
        abilityListeners.add(listener);
    }

    public void removeAbilityListener(AbilityListener listener) {
        abilityListeners.remove(listener);
    }
}
