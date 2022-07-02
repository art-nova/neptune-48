package game.gameobjects.particles;

import game.GamePanel;
import game.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Class that implements general capabilities of limited-time visual particles.
 *
 * @author Artem Novak
 */
public abstract class Particle extends GameObject {
    public static final int IDLE = 0, ANIMATING = 1;

    protected int state;
    protected int lifetime, framesLeft, fadeoutFrame;
    protected int width, height;
    protected BufferedImage image;

    /**
     * Constructs a new particle.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param lifetime number of frames it will display
     * @param fadeoutFrame frame on which the particle starts to fade
     * @param gp base GamePanel
     */
    public Particle(int x, int y, int lifetime, int fadeoutFrame, GamePanel gp) {
        super(x, y, gp);
        this.lifetime = lifetime;
        this.framesLeft = lifetime;
        this.fadeoutFrame = fadeoutFrame;
        state = ANIMATING;
    }

    @Override
    public void update() {
        framesLeft--;
        if (framesLeft <= 0) state = IDLE;
    }

    @Override
    public void render(Graphics2D g2d) {
        Composite composite = g2d.getComposite();
        if (framesLeft <= fadeoutFrame) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) framesLeft / fadeoutFrame));
        g2d.drawImage(image, (int) x, (int) y, null);
        g2d.setComposite(composite);
    }

    public void randomizeLocation(Rectangle bounds) {
        Random random = new Random();
        x = random.nextInt(bounds.x, bounds.x + bounds.width - width);
        y = random.nextInt(bounds.y, bounds.y + bounds.height - height);
    }

    public int getState() {
        return state;
    }
}
