package game.gameobjects.particles;

import game.GamePanel;
import game.utils.GamePanelGraphics;

import java.awt.*;
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
    private final List<TextParticle> particles = new ArrayList<>();
    private final Font textParticleFont;

    private int state;

    public ParticleManager(GamePanel gp) {
        this.gp = gp;
        this.textParticleFont = gp.getGameGraphics().getFont().deriveFont(Font.PLAIN, 30);
    }

    public void update() {
        for (TextParticle particle : particles) particle.update();
        particles.removeIf(x -> x.getState() == TextParticle.IDLE);
        if (particles.isEmpty()) state = IDLE;
        else state = ANIMATING;
    }

    public void render(Graphics2D g2d) {
        for (TextParticle particle : particles) particle.render(g2d);
    }

    /**
     * Generates a new text particle in a random location inside given bounds.
     *
     * @param text text
     * @param bounds bounds inside which it will be randomly positioned
     */
    public void addHealthChangeParticle(String text, Rectangle bounds) {
        TextParticle particle = new TextParticle(bounds.x, bounds.y, text, textParticleFont, GamePanelGraphics.ANIMATION_CYCLE * 5, gp);
        particle.randomizeLocation(bounds);
        particles.add(particle);
        state = ANIMATING;
    }

    public int getState() {
        return state;
    }
}
