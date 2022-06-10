package game.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Class that stores all graphic information necessary to render the logical state of a level,
 * such as textures, bounds, color palette etc.
 *
 * @author Artem Novak
 */
public class GamePanelGraphics extends ImageManager {

    // Constants needed for elements to draw themselves (scale is applied in runtime)
    // Board
    public static final int TILE_SIZE = 115;
    public static final int TILE_OFFSET = 8;
    public static final int TILE_PULSE_OFFSET = TILE_OFFSET/2;

    // Entity (one being attacked / repaired)
    public static final int ENTITY_WIDTH = TILE_SIZE*6;
    public static final int ENTITY_HEIGHT = (int)(TILE_SIZE*4.5f);

    // General
    public static final int ENTITY_BOARD_DISTANCE = TILE_SIZE/5;
    // How many frames a single animation takes (1 second == 60 frames)
    public static final int ANIMATION_CYCLE = 7;

    public final Font gameTextFont;

    public float scale = 1f; // NOT FULLY IMPLEMENTED YET

    private final String baseFolderPath;
    private final HashMap<String, Color> palette = new HashMap<>();
    {
        palette.put("textColor", Color.white);
        palette.put("highlight", new Color(255, 255, 255, 212));
        palette.put("boardBG", new Color(0, 18, 5));
        palette.put("boardCellBG", new Color(48, 94, 63));
        palette.put("tileBG0", new Color(28, 13, 0));
        palette.put("tileBG1", new Color(125, 125, 125));
        palette.put("tileBG2", new Color(136, 217, 121));
        palette.put("tileBG3", new Color(86, 214, 54));
        palette.put("tileBG4", new Color(230, 212, 122));
        palette.put("tileBG5", new Color(237, 202, 28));
        palette.put("tileBG6", new Color(124, 150, 235));
        palette.put("tileBG7", new Color(34, 84, 245));
        palette.put("tileBG8", new Color(172, 108, 224));
        palette.put("tileBG9", new Color(128, 27, 209));
        palette.put("tileBG10", new Color(242, 100, 75));
        palette.put("tileBG11", new Color(224, 11, 61));
    }
    /**
     * Initializes (but not loads!) the graphics manager.
     *
     * @param baseFolderPath path to the folder with all graphics for the level's game panel
     */
    public GamePanelGraphics(String baseFolderPath) {
        this.gameTextFont = new Font("Arial", Font.BOLD, TILE_SIZE/6);
        this.baseFolderPath = baseFolderPath;
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
     * @param boardRows rows of the board
     * @param boardCols columns of the board
     */
    public void load(int boardRows, int boardCols) throws IOException {
        loadBoard(boardRows, boardCols);
        loadEntity();
    }

    private void loadBoard(int boardRows, int boardCols) throws IOException{
        int tileSize = TILE_SIZE;
        int offset = TILE_OFFSET;
        int imageSize = tileSize*9/10;
        int imageOffset = tileSize/20;
        String tileFolderPath = baseFolderPath+"/tiles";

        // Loading tiles
        for (int i = 0; i <= 11; i++) {
            BufferedImage image = getScaledImage(getImage(tileFolderPath+"/tile"+i+".png"), imageSize, imageSize);
            BufferedImage tile = new BufferedImage(tileSize, tileSize, image.getType());
            Graphics2D g2d = (Graphics2D) tile.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getColor("tileBG"+i));
            g2d.fillRect(0, 0, tileSize, tileSize);
            g2d.drawImage(image, imageOffset, imageOffset, null);
            g2d.setFont(gameTextFont);
            g2d.setColor(Color.black);
            int textX = ((tileSize - g2d.getFontMetrics().stringWidth("LVL"+i))/2);
            g2d.drawString("LVL"+i, textX, tileSize - (int)(g2d.getFontMetrics().getDescent()*1.5));
            g2d.dispose();
            textures.put("tile"+i, tile);
        }
        textures.put("lockedOverlay", getScaledImage(getImage(tileFolderPath+"/lockedOverlay.png"), tileSize, tileSize));

        // Loading the actual board
        int boardWidth = tileSize * boardCols + offset * (boardCols + 1);
        int boardHeight = tileSize * boardRows + offset * (boardRows + 1);
        BufferedImage board = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) board.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(palette.get("boardBG"));
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setColor(palette.get("boardCellBG"));
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                g2d.fillRect(offset + j * (tileSize + offset), offset + i * (tileSize + offset), tileSize, tileSize);
            }
        }
        g2d.dispose();
        textures.put("boardBG", board);
    }

    private void loadEntity() throws IOException {
        //textures.put("entity", getScaledImage(getImage(baseFolderPath+"/entity.png"), (ENTITY_WIDTH), (ENTITY_HEIGHT)));
    }

}
