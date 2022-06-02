package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * Class that stores all graphic information necessary to render the logical state of a level,
 * such as textures, bounds, color palette etc.
 *
 * @author Artem Novak
 */
public class LevelGraphicsManager extends ImageManager {

    // Constants needed for elements to draw themselves (scale is applied in runtime)
    // Board
    public static final int TILE_SIZE = 100;
    public static final int TILE_OFFSET = TILE_SIZE/10;
    public static final int TILE_PULSE_OFFSET = TILE_OFFSET/2;

    // Entity (one being attacked / repaired)
    public static final int ENTITY_WIDTH = TILE_SIZE*6;
    public static final int ENTITY_HEIGHT = (int)(TILE_SIZE*4.5f);

    // Healthbar
    public static final int HEALTHBAR_WIDTH = ENTITY_WIDTH;
    public static final int HEALTHBAR_HEIGHT = TILE_SIZE/2;

    // Timer
    public static final int TIMER_X_OFFSET = TILE_OFFSET;
    public static final int TIMER_SIZE = TILE_SIZE;

    // General
    public static final int PANEL_ELEMENTS_OFFSET = TILE_SIZE/5;
    public static final int CORNER_ROUNDING = TILE_SIZE/10;
    // How many frames a single animation takes (1 second == 60 frames)
    public static final int ANIMATION_CYCLE = 10;

    public final Font uiFont;

    private final String tileFolderPath;
    private final String entityPath;
    private final String bgPath;
    private final HashMap<String, Color> palette = new HashMap<>();
    {
        palette.put("textColor", Color.white);
        palette.put("uiBG", new Color(0, 15, 3));
        palette.put("highlight", Color.white);
        palette.put("boardBG", new Color(0, 18, 5));
        palette.put("boardCellBG", new Color(48, 94, 63));
        palette.put("tileBG0", new Color(28, 13, 0));
        palette.put("tileBG1", new Color(125, 125, 125));
        palette.put("tileBG2", new Color(227, 227, 227));
        palette.put("tileBG3", new Color(136, 217, 121));
        palette.put("tileBG4", new Color(86, 214, 54));
        palette.put("tileBG5", new Color(230, 212, 122));
        palette.put("tileBG6", new Color(237, 202, 28));
        palette.put("tileBG7", new Color(124, 150, 235));
        palette.put("tileBG8", new Color(34, 84, 245));
        palette.put("tileBG9", new Color(172, 108, 224));
        palette.put("tileBG10", new Color(128, 27, 209));
        palette.put("tileBG11", new Color(224, 11, 61));
    }
    /**
     * Initializes (but not loads!) the graphics manager.
     * Notes:
     * "Entity" is the entity interacted with during core gameplay, enemy or ally depending on the mode.
     *
     * @param tileFolderPath path to folder with level's tile pack (must contain images "tile0.png", ..., "tile11.png", "immovableOverlay.png")
     * @param bonusFolderPath path to folder with level's bonus pack (must contain ".png" files for all bonuses that have to be loaded)
     * @param obstacleFolderPath path to folder with level's obstacle pack (must contain ".png" files for all obstacles that have to be loaded)
     * @param bgPath path to level background image
     * @param entityPath path to top entity image
     * @param entityHealthColor color of the health indicator of entity at the top
     * @param maxBonusSize maximum absolute bonus size in pixels (for optimization)
     * @param maxObstacleSize maximum absolute bonus size in pixels (for optimization)
     */
    public LevelGraphicsManager(String tileFolderPath, String bonusFolderPath, String obstacleFolderPath,
                                String bgPath, String entityPath,
                                Color entityHealthColor, int maxBonusSize, int maxObstacleSize) {
        this.uiFont = new Font("Arial", Font.BOLD, TILE_SIZE/6);
        this.tileFolderPath = tileFolderPath;
        this.bgPath = bgPath;
        this.entityPath = entityPath;

        palette.put("entityHealthColor", entityHealthColor);
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
     * Loads the actual textures using supplied non-graphical data.
     *
     * @param bonusNameIDs available bonuses' NameIDs
     * @param obstacleNameIDs available obstacles' NameIDs
     * @param boardRows rows of the board
     * @param boardCols columns of the board
     */
    public void load(int boardRows, int boardCols, Set<String> bonusNameIDs, Set<String> obstacleNameIDs) throws IOException {
        loadBoard(boardRows, boardCols);
//        loadBG();
//        loadEntity();
//        loadBonuses();
//        loadObstacles();
    }

    private void loadBoard(int boardRows, int boardCols) throws IOException{
        int tileSize = TILE_SIZE;
        int offset = TILE_OFFSET;
        int imageSize = tileSize*9/10;
        int imageOffset = tileSize/20;

        // Loading tiles
        for (int i = 0; i <= 11; i++) {
            BufferedImage image = getScaledImage(ImageIO.read(new File(tileFolderPath+"/tile"+i+".png")), imageSize, imageSize);
            BufferedImage tile = new BufferedImage(tileSize, tileSize, image.getType());
            Graphics2D g2d = (Graphics2D) tile.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getColor("tileBG"+i));
            g2d.fillRoundRect(0, 0, tileSize, tileSize, CORNER_ROUNDING, CORNER_ROUNDING);
            g2d.drawImage(image, imageOffset, imageOffset, null);
            g2d.setFont(uiFont);
            g2d.setColor(Color.black);
            int textX = ((tileSize - g2d.getFontMetrics().stringWidth("LVL"+i))/2);
            g2d.drawString("LVL"+i, textX, tileSize - (int)(g2d.getFontMetrics().getDescent()*1.5));
            g2d.dispose();
            textures.put("tile"+i, tile);
        }
        textures.put("lockedOverlay", getScaledImage(ImageIO.read(new File(tileFolderPath+"/lockedOverlay.png")), tileSize, tileSize));

        // Loading the actual board
        int boardWidth = tileSize * boardCols + offset * (boardCols + 1);
        int boardHeight = tileSize * boardRows + offset * (boardRows + 1);
        BufferedImage board = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) board.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(palette.get("boardBG"));
        g2d.fillRoundRect(0, 0, boardWidth, boardHeight, CORNER_ROUNDING, CORNER_ROUNDING);
        g2d.setColor(palette.get("boardCellBG"));
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                g2d.fillRoundRect(offset + j * (tileSize + offset), offset + i * (tileSize + offset), tileSize, tileSize, CORNER_ROUNDING, CORNER_ROUNDING);
            }
        }
        g2d.dispose();
        textures.put("boardBG", board);
    }

    private void loadEntity() throws IOException {
        textures.put("entity", getScaledImage(getImage(entityPath), (ENTITY_WIDTH), (ENTITY_HEIGHT)));
    }

    private void loadBG() throws IOException {
        textures.put("bg", getImage(bgPath));
    }

    private void loadBonuses() {
        // TODO
    }

    private void loadObstacles() {
        // TODO
    }

}
