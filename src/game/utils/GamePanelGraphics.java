package game.utils;

import game.GamePanel;
import models.App;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class that stores all graphic information necessary to render the logical state of a level,
 * such as textures, bounds, color palette etc.
 *
 * @author Artem Novak
 */
public class GamePanelGraphics {

    // How many frames a single animation takes (1 second == 60 frames)
    public static final int ANIMATION_CYCLE = 10;

    // Entity (one being attacked / repaired)
    public static final int ENTITY_WIDTH = 480;
    public static final int ENTITY_HEIGHT = 250;
    public static final int ENTITY_BOARD_DISTANCE = 40;
    
    // Board
    private final int tileSize;
    private final int tileOffset;
    private final int tilePulseOffset;

    private final HashMap<String, Color> palette = new HashMap<>();

    private final Font font;
    private HashMap<String, BufferedImage> textures = new HashMap<>();

    /**
     * Initializes (but not loads!) the graphics manager.
     *
     * @param tileSize size of one tile in pixels
     * @param tileOffset offset between tiles (and between a tile and the board edge) in pixels
     * @param boardRows number of rows (height)
     * @param boardCols number of columns (width)
     * @param gameMode {@link GamePanel#GAME_MODE_ATTACK} or {@link GamePanel#GAME_MODE_REPAIR}
     * @param entityIndex index of the entity texture
     */
    public GamePanelGraphics(int tileSize, int tileOffset, int boardRows, int boardCols, int gameMode, int entityIndex) throws IOException {
        font = App.lexenDeca.deriveFont(Font.PLAIN, 25);

        // Palette
        palette.put("textColor", Color.white);
        palette.put("highlight", new Color(255, 255, 255, 64));
        palette.put("darken", new Color(0, 0, 0, 128));
        palette.put("boardBG", new Color(23,62,31));
        palette.put("boardCellBG", new Color(42,113,56));
        palette.put("damageOverlay", new Color(156, 31, 26, 128));
        palette.put("healOverlay", new Color(112, 212, 40, 128));

        // Sizes
        this.tileSize = tileSize;
        this.tileOffset = tileOffset;
        tilePulseOffset = tileOffset / 2;

        // Loading
        loadBoard(boardRows, boardCols, gameMode);
        loadEntity(entityIndex);
    }

    /**
     * Returns color from palette by given NameID.
     *
     * @param nameID color NameID
     * @return corresponding color from palette
     */
    public Color getColor(String nameID) {
        if (!palette.containsKey(nameID)) throw new IllegalArgumentException("Unknown color " + nameID);
        return palette.get(nameID);
    }

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

    private void loadBoard(int boardRows, int boardCols, int gameMode) throws IOException{
        String tileFolderPath;
        if (gameMode == GamePanel.GAME_MODE_ATTACK) {
            tileFolderPath = "resources/images/tiles_attack/";
        }
        else {
            tileFolderPath = "resources/images/tiles_repair/";
        }

        // Loading tiles
        for (int i = 0; i <= 11; i++) {
            BufferedImage image = getScaledImage(getImage(tileFolderPath+"tile"+i+".png"), tileSize, tileSize);
            textures.put("tile"+i, image);
        }
        textures.put("lockedOverlay", getScaledImage(getImage(tileFolderPath+"lockedOverlay.png"), tileSize, tileSize));

        // Loading the actual board
        int boardWidth = tileSize * boardCols + tileOffset * (boardCols + 1);
        int boardHeight = tileSize * boardRows + tileOffset * (boardRows + 1);
        BufferedImage board = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) board.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(palette.get("boardBG"));
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setColor(palette.get("boardCellBG"));
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                g2d.fillRect(tileOffset + j * (tileSize + tileOffset), tileOffset + i * (tileSize + tileOffset), tileSize, tileSize);
            }
        }
        g2d.dispose();
        textures.put("boardBG", board);
    }

    private void loadEntity(int entityIndex) throws IOException {
        BufferedImage entity = getScaledImage(getImage("resources/images/entities/entity" + entityIndex + ".png"), ENTITY_WIDTH, ENTITY_HEIGHT);
        textures.put("entity", entity);
        textures.put("entityDamaged", addColorOverlay(entity, palette.get("damageOverlay")));
        textures.put("entityHealed", addColorOverlay(entity, palette.get("healOverlay")));

        for (int i = 0; i < 4; i++) {
            textures.put("explosion"+i, getScaledImage(getImage("resources/images/entities/explosion" + i + ".png"), ENTITY_HEIGHT, ENTITY_HEIGHT));
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getTileOffset() {
        return tileOffset;
    }

    public int getTilePulseOffset() {
        return tilePulseOffset;
    }

    public Font getFont() {
        return font;
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
