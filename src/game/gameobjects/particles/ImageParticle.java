package game.gameobjects.particles;

import game.GamePanel;

import java.awt.image.BufferedImage;

/**
 * Class that implements capabilities for adding particles consisting from images changing with a certain frequency.
 *
 * @author Artem Novak
 */
public class ImageParticle extends Particle {
    private final BufferedImage[] images;
    private final int changeFrequency;
    private int currentIndex;
    private int frameCounter;

    public ImageParticle(int x, int y, int lifetime, int fadeoutFrame, BufferedImage[] images, int changeFrequency, GamePanel gp) {
        super(x, y, lifetime, fadeoutFrame, gp);
        this.images = images;
        this.changeFrequency = changeFrequency;
        image = images[currentIndex];
    }

    @Override
    public void update() {
        super.update();
        frameCounter++;
        if (frameCounter >= changeFrequency && currentIndex < images.length - 1) {
            frameCounter = 0;
            currentIndex++;
            image = images[currentIndex];
        }
    }
}
