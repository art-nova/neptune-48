package game;

import java.awt.*;

/**
 * Class that stores information about the entity above the board.
 *
 * @author Artem Novak
 */
public class Entity extends GameObject {
    private final int maxHealth;
    private int health;

    /**
     * Creates a new Entity.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param maxHealth max health
     * @param gp root GamePanel
     */
    public Entity(int x, int y, int maxHealth, GamePanel gp) {
        super(x, y, gp);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(gp.graphics.getTexture("entity"), (int)x, (int)y, null);
    }

    /**
     * Adds given number to entity's health.
     *
     * @param delta change in health
     */
    public void changeHealth(int delta) {
        health += delta;
        if (health >= maxHealth) health = maxHealth;
        else if (health <= 0) health = 0;
    }
}
