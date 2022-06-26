package game.gameobjects.particles;

import game.GamePanel;
import game.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TextParticle extends GameObject {
    public static final int IDLE = 0, ANIMATING = 1;

    private String text;
    private Font font;
    private BufferedImage image;
    private int width, height;
    private int overallFrames;
    private int framesLeft;
    private int state;

    public TextParticle(int x, int y, String text, Font font, int lifeTime, GamePanel gp) {
        super(x, y, gp);
        this.text = text;
        this.font = font;
        image = generateTextImage(text, font);
        this.overallFrames = lifeTime;
        this.framesLeft = lifeTime;
        state = ANIMATING;
    }

    private BufferedImage generateTextImage(String text, Font font) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        width = fm.stringWidth(text);
        height = fm.getHeight();
        g2d.dispose();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) result.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setFont(font);
        g2d.setColor(graphics.getColor("textColor"));
        g2d.drawString(text, 0, g2d.getFontMetrics().getAscent());
        g2d.dispose();
        return result;
    }

    @Override
    public void update() {
        framesLeft--;
        if (framesLeft <= 0) state = IDLE;
    }

    @Override
    public void render(Graphics2D g2d) {
        Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) framesLeft / overallFrames));
        g2d.drawImage(image, (int) x, (int) y, null);
        g2d.setComposite(composite);
    }

    public void setText(String text) {
        this.text = text;
        generateTextImage(text, font);
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public int getState() {
        return state;
    }

    public void randomizeLocation(Rectangle bounds) {
        Random random = new Random();
        x = random.nextInt(bounds.x, bounds.x + bounds.width - width);
        y = random.nextInt(bounds.y, bounds.y + bounds.height - height);
    }
}
