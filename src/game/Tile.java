package game;

import game.utils.GamePanelGraphics;
import java.awt.*;

/**
 * Class that implements logical and rendering functionality of the board tile.
 *
 * @author Artem Novak
 */
public class Tile extends GameObject {
    // Possible animation states.
    public static final int IDLE = 0, MOVING = 1, MERGING = 2, PULSATING = 3, GENERATING = 4;

    private final GamePanelGraphics graphics;

    private int state = GENERATING;
    private int level;
    private boolean locked = false;
    private boolean visible = false;

    private float speedX, speedY;
    private int targetX, targetY;

    private float visualOffset;
    private float speedVisualOffset;
    private int targetVisualOffset;

    private int animationFramesLeft;

    public Tile(int x, int y, int level, GamePanel gp) {
        super(x, y, gp);
        this.graphics = gp.getGameGraphics();
        this.targetX = x;
        this.targetY = y;
        this.level = level;
    }

    /**
     * Updates all logical values of the tile.
     */
    public void update() {
        if (state == MOVING || state == MERGING) {
            x += speedX;
            y += speedY;
            animationFramesLeft--;
            if (state == MERGING && animationFramesLeft <= 0) pulse(0, GamePanelGraphics.TILE_PULSE_OFFSET);;
        }
        else if (state == PULSATING) {
            if (targetVisualOffset > visualOffset) visualOffset += speedVisualOffset;
            else visualOffset -= speedVisualOffset;
            animationFramesLeft--;
            if (Math.abs(targetVisualOffset - visualOffset) <= speedVisualOffset/2) {
                targetVisualOffset = 0;
            }
        }
        else if (state == GENERATING) {
            if (animationFramesLeft <= 0) {
                visible = true;
                pulse(-GamePanelGraphics.TILE_SIZE / 2, GamePanelGraphics.TILE_PULSE_OFFSET);
            }
        }
        if (state != IDLE && animationFramesLeft <= 0) flush();
    }

    public void render(Graphics2D g2d) {
        if (visible) {
            int screenX = (int)x;
            int screenY = (int)y;

            if (state == PULSATING) {
                int size = (GamePanelGraphics.TILE_SIZE + (int)(visualOffset * 2));
                int offset = (int)visualOffset;
                g2d.drawImage(graphics.getTexture("tile"+level), screenX - offset, screenY - offset, size, size, null);
            }
            else if (state == MERGING) {
                g2d.drawImage(graphics.getTexture(("tile"+(level-1))), screenX, screenY, null);
            }
            else {
                g2d.drawImage(graphics.getTexture(("tile"+level)), screenX, screenY, null);
                if (locked) g2d.drawImage(graphics.getTexture(("lockedOverlay")), screenX, screenY, null);
            }
        }
    }

    /**
     * Moves the tile to the state expected at the end of current animation cycle and switches animation state to STATIC.
     */
    public void flush() {
        x = targetX;
        y = targetY;
        speedX = 0;
        speedY = 0;
        speedVisualOffset = 0;
        targetVisualOffset = 0;
        visualOffset = 0;
        if (state == GENERATING) visible = true;
        animationFramesLeft = 0;
        state = IDLE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0 || state > 4) throw new IllegalArgumentException("Tile does not support state " + state);
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
        level++;
    }

    private void startAnimationCycle() {
        animationFramesLeft = GamePanelGraphics.ANIMATION_CYCLE;
    }

}
