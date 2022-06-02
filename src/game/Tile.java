package game;

import utils.GamePanelGraphics;
import java.awt.*;

/**
 * Class that implements logical and rendering functionality of the board tile.
 *
 * @author Artem Novak
 */
public class Tile implements IRenderable{
    // Possible animation states.
    public static final int STATIC = 0, MOVING = 1, MERGING = 2, PULSATING = 3, GENERATING = 4;

    public int state = GENERATING;
    public int level;
    public boolean locked = false;
    public boolean visible = false;

    private float x, y;
    private float speedX, speedY;
    private int targetX, targetY;

    private float visualOffset;
    private float speedVisualOffset;
    private int targetVisualOffset;

    private int animationFramesLeft;

    private final GamePanel gp;
    private final Board board;

    public Tile(int x, int y, int level, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.level = level;
        this.gp = gp;
        this.board = gp.board;
    }

    /**
     * Updates all logical values of the tile.
     */
    public void update() {
        if (state == MOVING || state == MERGING) {
            board.state = Board.ANIMATING;
            x += speedX;
            y += speedY;
            animationFramesLeft--;
            if (state == MERGING && animationFramesLeft <= 0) upgrade();
        }
        else if (state == PULSATING) {
            board.state = Board.ANIMATING;
            if (targetVisualOffset > visualOffset) visualOffset += speedVisualOffset;
            else visualOffset -= speedVisualOffset;
            animationFramesLeft--;
            if (Math.abs(targetVisualOffset - visualOffset) <= speedVisualOffset/2) {
                targetVisualOffset = 0;
            }
        }
        else if (state == GENERATING && animationFramesLeft <= 0) {
            visible = true;
            pulse(-GamePanelGraphics.TILE_SIZE/2, GamePanelGraphics.TILE_PULSE_OFFSET);
        }
        if (state != STATIC && animationFramesLeft <= 0) flush();
    }

    public void render(Graphics2D g2d) {
        if (visible) {
            int screenX = (int)x;
            int screenY = (int)y;

            if (state == PULSATING) {
                int size = (GamePanelGraphics.TILE_SIZE + (int)(visualOffset * 2));
                int offset = (int)visualOffset;
                g2d.drawImage(gp.graphicsManager.getTexture("tile"+level), screenX - offset, screenY - offset, size, size, null);
            }
            else {
                g2d.drawImage(gp.graphicsManager.getTexture(("tile"+level)), screenX, screenY, null);
                if (locked) g2d.drawImage(gp.graphicsManager.getTexture(("lockedOverlay")), screenX, screenY, null);
            }
        }
    }

    /**
     * Moves the tile to the state expected at the end of current animation cycle and switches animation state to STATIC.
     */
    public void flush() {
        speedVisualOffset = 0;
        targetVisualOffset = 0;
        visualOffset = 0;
        x = targetX;
        y = targetY;
        speedX = 0;
        speedY = 0;
        animationFramesLeft = 0;
        state = STATIC;
    }

    /**
     * Initiates tile going through one animation cycle of pulsing with specified starting and peak offsets, ending at offset 0.
     * Offset is the number of pixels the tile's visual borders are expanded outward from logical borders in all directions.
     * For example, if offset 2 is specified, tile will be rendered 4 pixels wider and 4 pixels higher while maintaining its center.
     * Negative values make the tile shrink.
     *
     * @param startOffset starting offset
     * @param peakOffset peak offset
     */
    public void pulse(int startOffset, int peakOffset) {
        startAnimationCycle();
        visualOffset = startOffset;
        speedVisualOffset = (float)(Math.abs(startOffset - peakOffset) + Math.abs(peakOffset))/ GamePanelGraphics.ANIMATION_CYCLE;
        targetVisualOffset = peakOffset;
        state = PULSATING;
    }

    /**
     * Increases tile level and initiates outward pulse animation.
     */
    public void upgrade() {
        if (level < 11) {
            level++;
            pulse(0, GamePanelGraphics.TILE_PULSE_OFFSET);
        }
    }

    /**
     * Decreases tile level and initiates inward pulse animation.
     */
    public void downgrade() {
        if (level > 1) {
            level--;
            pulse(0, -GamePanelGraphics.TILE_PULSE_OFFSET);
        }
    }

    /**
     * Initiates tile's movement towards given point.
     *
     * @param target target point
     */
    public void moveTowards(Point target) {
        startAnimationCycle();
        targetX = target.x;
        targetY = target.y;
        speedX = (targetX - this.x)/animationFramesLeft;
        speedY = (targetY - this.y)/animationFramesLeft;
        state = MOVING;
    }

    /**
     * Schedules tile for merging.
     */
    public void makeMergeBase() {
        startAnimationCycle();
        state = MERGING;
    }

    private void startAnimationCycle() {
        animationFramesLeft = GamePanelGraphics.ANIMATION_CYCLE;
        board.state = Board.ANIMATING;
    }

}
