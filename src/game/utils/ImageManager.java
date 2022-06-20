package game.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Abstract class that provides framework for image caching classes (does not have its own methods for caching).
 *
 * @author Artem Novak
 */
public abstract class ImageManager {
    protected HashMap<String, BufferedImage> textures = new HashMap<>();

    /**
     * Returns texture by the given NameID (coincides with texture filename).
     *
     * @param nameID texture NameID
     * @return BufferedImage of the texture
     */
    public BufferedImage getTexture(String nameID) {
        if (!textures.containsKey(nameID)) throw new IllegalArgumentException("Unknown texture " + nameID);
        return textures.get(nameID);
    }

    /**
     * Scales BufferedImage, providing higher-quality downscaling.
     *
     * @param image image to scale
     * @param width width of the scaled instance
     * @param height height of the scaled instance
     * @return scaled instance of the loaded image
     */
    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = image;
        int tempWidth = image.getWidth(), tempHeight = image.getHeight();
        if (width < image.getWidth() || height <= image.getHeight()) {
            do {
                tempWidth /= 2;
                tempHeight /= 2;
                if (tempWidth < width) tempWidth = width;
                if (tempHeight < height) tempHeight = height;
                BufferedImage tmp = new BufferedImage(tempWidth, tempHeight, image.getType());
                Graphics2D g2d = (Graphics2D) tmp.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(scaledImage, 0, 0, tempWidth, tempHeight, null);
                g2d.dispose();

                scaledImage = tmp;
            } while (tempWidth != width || tempHeight != height);
        }
        return scaledImage;
    }

    /**
     * Reads {@link BufferedImage} from a filepath.
     *
     * @param filepath path to the image
     * @return {@link BufferedImage} that was loaded
     * @throws IOException if fails to load the image
     */
    public static BufferedImage getImage(String filepath) throws IOException {
        BufferedImage image = ImageIO.read(new File(filepath));
        if (image == null) throw new IOException("Failed to load image from " + filepath);
        return image;
    }

    /**
     * Adds colored overlay to the image's filled part (transparent parts remain unchanged).
     *
     * @param image original image
     * @param color color (possibly semi-transparent)
     * @return {@link BufferedImage} with added overlay
     */
    public static BufferedImage addColorOverlay(BufferedImage image, Color color) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) result.getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(color);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();
        return result;
    }
}
