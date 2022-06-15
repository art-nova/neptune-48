package game.gameobjects;

import game.GamePanel;

import java.awt.*;

/**
 * Interface that describes a generic game object.
 *
 * @author Artem Novak
 */
public abstract class GameObject {
    protected float x;
    protected float y;
    protected GamePanel gp;

    /**
     * Creates a new abstract game object.
     *
     * @param x x at the moment of creation
     * @param y y at the moment of creation
     * @param gp the root GamePanel of this object
     */
    public GameObject(int x, int y, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.gp = gp;
    }

    /**
     * Updates the object's logical state.
     */
    public abstract void update();

    /**
     * Renders the object on a Graphics2D instance.
     *
     * @param g2d Graphics2D instance for rendering
     */
    public abstract void render(Graphics2D g2d);

    /**
     * @return x value relative to the game panel
     */
    public float getX() {
        return x;
    }

    /**
     * @return y value relative to the game panel
     */
    public float getY() {
        return y;
    }

    /**
     * @param x x value relative to the game panel
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y y value relative to the game panel
     */
    public void setY(float y) {
        this.y = y;
    }
}
