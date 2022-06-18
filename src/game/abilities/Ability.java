package game.abilities;

import game.GameModifier;
import game.GamePanel;
import game.UIDataHolder;
import game.events.AbilityListener;
import game.events.UIDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that describes an abstract player-based useful ability (as opposed to
 * harmful implementations of {@link game.obstacles.Obstacle} coming from outside)
 *
 * @author Artem Novak
 */
public abstract class Ability extends GameModifier implements UIDataHolder {
    protected final List<UIDataListener> uiDataListeners = new ArrayList<>();
    protected final List<AbilityListener> abilityListeners = new ArrayList<>();
    protected final AbilityManager abilityManager;

    public Ability(GamePanel gp, AbilityManager abilityManager) {
        super(gp);
        this.abilityManager = abilityManager;
    }

    @Override
    public void addUIDataListener(UIDataListener listener) {
        uiDataListeners.add(listener);
    }

    @Override
    public void removeUIDataListener(UIDataListener listener) {
        uiDataListeners.remove(listener);
    }

    @Override
    public void setState(int state) {
        if (this.state != state) {
            super.setState(state);
            for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
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
