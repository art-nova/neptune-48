package game.obstacles;

import game.GamePanel;
import game.gameobjects.Entity;

/**
 * Class that implements "healEntity" obstacle.
 *
 * @author Artem Novak
 */
public class HealEntity extends Obstacle {
    public static final int HEAL_PERCENTAGE = 5;

    private final Entity entity;
    private final long healing;

    public HealEntity(GamePanel gp) {
        super(gp);
        this.entity = gp.getEntity();
        healing = (long)(entity.getMaxHealth() * HEAL_PERCENTAGE / 100f);
    }

    @Override
    public String getNameID() {
        return "damageEntity";
    }

    @Override
    public void startApplication() {
        entity.takeHealing(healing);
        entity.animateHealing(healing);
    }

    @Override
    protected boolean determineApplicability() {
        return super.determineApplicability() && entity.getHealth() < entity.getMaxHealth();
    }
}
