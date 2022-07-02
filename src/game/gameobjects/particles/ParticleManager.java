package game.gameobjects.particles;

import game.GamePanel;
import game.utils.GamePanelGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing in-game visual particles.
 *
 * @author Artem Novak
 */
public class ParticleManager {
    public static final int IDLE = 0, ANIMATING = 1;

    private final GamePanel gp;
    private final GamePanelGraphics graphics;
    private final List<Particle> particles = new ArrayList<>();
    private final Font textParticleFont;

    private int state;

    public ParticleManager(GamePanel gp) {
        this.gp = gp;
        this.graphics = gp.getGameGraphics();
        this.textParticleFont = gp.getGameGraphics().getFont().deriveFont(Font.PLAIN, 30);
    }

    public void update() {
        for (Particle particle : particles) particle.update();
        particles.removeIf(x -> x.getState() == Particle.IDLE);
        if (particles.isEmpty()) state = IDLE;
        else state = ANIMATING;
    }

    public void render(Graphics2D g2d) {
        for (Particle particle : particles) {
            particle.render(g2d);
        }
    }

    /**
     * Generates a new text particle in a random location inside given bounds.
     *
     * @param text text
     * @param bounds bounds inside which it will be randomly positioned
     */
    public void addHealthChangeParticle(String text, Rectangle bounds) {
        TextParticle particle = new TextParticle(bounds.x, bounds.y, GamePanelGraphics.ANIMATION_CYCLE * 5, text, textParticleFont, gp);
        particle.randomizeLocation(bounds);
        particles.add(particle);
        state = ANIMATING;
    }

    public void addExplosionParticle(int x, int y) {
        BufferedImage[] images = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            images[i] = graphics.getTexture("explosion"+i);
        }
        ImageParticle particle = new ImageParticle(x, y, GamePanelGraphics.ANIMATION_CYCLE * 2, GamePanelGraphics.ANIMATION_CYCLE * 6 / 5, images, GamePanelGraphics.ANIMATION_CYCLE / 2, gp);
        particles.add(particle);
        state = ANIMATING;
    }

    public int getState() {
        return state;
    }
}
