package game;

import game.events.EntityListener;
import game.utils.GamePanelGraphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class that stores information about the entity above the board.
 *
 * @author Artem Novak
 */
public class Entity extends GameObject {
    private final long maxHealth;
    private long health;
    private final ArrayList<EntityListener> listeners = new ArrayList<>();
    private final GamePanelGraphics graphics;

    /**
     * Creates a new Entity.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param maxHealth max health
     * @param gp root GamePanel
     */
    public Entity(int x, int y, long maxHealth, GamePanel gp) {
        super(x, y, gp);
        this.graphics = gp.getGameGraphics();
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(graphics.getTexture("entity"), (int)x, (int)y, null);
    }

    /**
     * Adds given number to entity's health.
     *
     * @param delta change in health
     */
    public void changeHealth(long delta) {
        long oldHealth = health;
        health += delta;
        if (health >= maxHealth) health = maxHealth;
        else if (health <= 0) health = 0;
        for (EntityListener listener : listeners) listener.onHealthChanged(oldHealth, health);
    }

    public void addEntityListener(EntityListener listener) {
        listeners.add(listener);
    }

    public void removeEntityListener(EntityListener listener) {
        listeners.remove(listener);
    }
}
