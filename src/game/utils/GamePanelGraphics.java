package game.utils;

import game.GamePanel;

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
public class GamePanelGraphics extends ImageManager {

    // How many frames a single animation takes (1 second == 60 frames)
    public static final int ANIMATION_CYCLE = 10;
    
    // Board
    private final int tileSize;
    private final int tileOffset;
    private final int tilePulseOffset;

    // Entity (one being attacked / repaired)
    private final int entityWidth;
    private final int entityHeight;

    // General
    private final int entityBoardDistance;

    private final HashMap<String, Color> palette = new HashMap<>();

    private Font font;

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
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Rubik-VariableFont_wght.ttf"));
        }
        catch (FontFormatException e) {
            e.printStackTrace();
            font = new Font(null);
        }
        font = font.deriveFont(Font.PLAIN, 20);

        // Palette
        palette.put("textColor", Color.white);
        palette.put("highlight", new Color(255, 255, 255, 64));
        palette.put("darken", new Color(0, 0, 0, 128));
        palette.put("boardBG", new Color(0, 18, 5));
        palette.put("boardCellBG", new Color(48, 94, 63));
        palette.put("damageOverlay", new Color(156, 31, 26, 128));
        palette.put("healOverlay", new Color(112, 212, 40, 128));

        // Sizes
        this.tileSize = tileSize;
        this.tileOffset = tileOffset;
        tilePulseOffset = tileOffset / 2;
        entityWidth = tileSize * 5 + tileOffset * 6;
        entityHeight = tileSize * 2;
        entityBoardDistance = tileSize / 5;

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
        BufferedImage entity = getScaledImage(getImage("resources/images/entities/entity" + entityIndex + ".png"), entityWidth, entityHeight);
        textures.put("entity", entity);
        textures.put("entityDamaged", addColorOverlay(entity, palette.get("damageOverlay")));
        textures.put("entityHealed", addColorOverlay(entity, palette.get("healOverlay")));
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

    public int getEntityWidth() {
        return entityWidth;
    }

    public int getEntityHeight() {
        return entityHeight;
    }

    public int getEntityBoardDistance() {
        return entityBoardDistance;
    }

    public Font getFont() {
        return font;
    }
}
