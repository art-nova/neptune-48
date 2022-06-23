package game.obstacles;

import game.GamePanel;
import game.gameobjects.Entity;

/**
 * Class that implements "damageEntity" obstacle.
 * <br>
 * This obstacle is intended for {@link game.GamePanel#GAME_MODE_ATTACK} only.
 *
 * @author Artem Novak
 */
public class DamageEntity extends Obstacle {
    public static final int DAMAGE_PERCENTAGE = 5;

    private final Entity entity;
    private final long damage;

    public DamageEntity(GamePanel gp) {
        super(gp);
        this.entity = gp.getEntity();
        damage = (long)(entity.getMaxHealth() * DAMAGE_PERCENTAGE / 100f);
    }

    @Override
    public String getNameID() {
        return "damageEntity";
    }

    @Override
    public void startApplication() {
        entity.takeDamage(damage);
        entity.takeDamage(damage);
    }

    @Override
    protected boolean determineApplicability() {
        return true;
    }
}
