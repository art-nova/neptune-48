package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    // LEGACY VERSION OF THE SCALING ALGORITHM
//    /**
//     * Loads a picture by filepath and scales it.
//     *
//     * @param filepath path in format "/images/.../image.png"
//     * @param width width of the scaled instance
//     * @param height height of the scaled instance
//     * @return scaled instance of the loaded image or null
//     * @throws IOException if image reading encounters issues
//     */
//    protected BufferedImage loadScaledImage(String filepath, int width, int height) throws IOException {
//        BufferedImage image = getImage(filepath);
//        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
//        Graphics2D g2d = (Graphics2D) scaledImage.getGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g2d.drawImage(image, 0, 0, width, height, null);
//        g2d.dispose();
//        return scaledImage;
//    }

    /**
     * Scales BufferedImage, providing higher-quality downscaling.
     *
     * @param image image to scale
     * @param width width of the scaled instance
     * @param height height of the scaled instance
     * @return scaled instance of the loaded image
     */
    public BufferedImage getScaledImage(BufferedImage image, int width, int height) {
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

    public BufferedImage getImage(String filepath) throws IOException {
        InputStream resource = getClass().getResourceAsStream(filepath);
        if (resource == null) throw new IOException("Failed to find image resource at " + filepath);
        BufferedImage image = ImageIO.read(resource);
        if (image == null) throw new IOException("Failed to load image from " + filepath);
        return image;
    }
}
