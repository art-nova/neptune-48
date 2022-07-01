package game.gameobjects;

import game.GameLogicException;
import game.GamePanel;
import game.utils.GamePanelGraphics;
import misc.AudioManager;

import java.awt.*;

/**
 * Class that implements logical and rendering functionality of the board tile.
 *
 * @author Artem Novak
 */
public class Tile extends GameObject {
    // Possible animation states.
    public static final int IDLE = 0, MOVING = 1, MERGING = 2, PULSATING = 3, GENERATING = 4;

    private int state = GENERATING;
    private int level;
    private int levelVisualOffset;
    private boolean locked = false;
    private boolean visuallyLocked = false;
    private boolean visible = false;

    private float speedX, speedY;
    private int targetX, targetY;

    private float visualOffset;
    private float speedVisualOffset;
    private int targetVisualOffset;

    private int animationFramesLeft;
    private boolean lingering;

    public Tile(int x, int y, int level, GamePanel gp) {
        super(x, y, gp);
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
            if (state == MERGING && animationFramesLeft <= 0) {
                setLevelVisualOffset(levelVisualOffset+1);
                upgradeAnimation();
            }
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
                pulse(graphics.getTileSize() / 2, -graphics.getTilePulseOffset());
            }
        }
        if (state != IDLE && animationFramesLeft <= 0) flush();
    }

    public void render(Graphics2D g2d) {
        if (visible) {
            int screenX = (int)x;
            int screenY = (int)y;

            if (state == PULSATING) {
                int size = (graphics.getTileSize() - (int)(visualOffset * 2));
                int offset = (int)visualOffset;
                g2d.drawImage(graphics.getTexture("tile"+(level + levelVisualOffset)), screenX + offset, screenY + offset, size, size, null);
            }
            else {
                g2d.drawImage(graphics.getTexture(("tile"+(level + levelVisualOffset))), screenX, screenY, null);
                if (visuallyLocked) g2d.drawImage(graphics.getTexture(("lockedOverlay")), screenX, screenY, null);
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
        if (state == MERGING) setLevelVisualOffset(levelVisualOffset + 1);
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

    /**
     * Sets tile level.
     *
     * @param level new level
     */
    public void setLevel(int level) {
        if (level < 0 || level > 11) throw new GameLogicException("Trying to set tile to nonexistent level " + level);
        this.level = level;
    }

    public int getLevelVisualOffset() {
        return levelVisualOffset;
    }

    public void setLevelVisualOffset(int levelVisualOffset) {
        if (level + levelVisualOffset < 0 || level + levelVisualOffset > 11) throw new GameLogicException("Trying to set a level offset that will visually amount to nonexistent level " + level + levelVisualOffset);
        this.levelVisualOffset = levelVisualOffset;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isVisuallyLocked() {
        return visuallyLocked;
    }

    public void setVisuallyLocked(boolean visuallyLocked) {
        this.visuallyLocked = visuallyLocked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns whether this tile is lingering.
     * Lingering tiles do not stop displaying on the board, even if they do not have a logical place there and are in
     * {@link Tile#IDLE} state
     *
     * @return true if the tile is lingering
     */
    public boolean isLingering() {
        return lingering;
    }

    /**
     * Changes whether this tile is lingering or not.
     * will make the state {@link Tile#IDLE}.
     *
     * @param lingering new value for whether tile is suspended or not.
     */
    public void setLingering(boolean lingering) {
        this.lingering = lingering;
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
     * Initiates outward pulse animation and lets tile display its true level.
     */
    public void upgradeAnimation() {
        AudioManager.playSFX("upgrade");
        pulse(0, -graphics.getTilePulseOffset());
    }

    /**
     * Initiates inward pulse animation and lets tile display its true level.
     */
    public void downgradeAnimation() {
        pulse(0, graphics.getTilePulseOffset());
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
        setLevelVisualOffset(levelVisualOffset-1);
    }

    public void dispose() {
        startAnimationCycle();
        visualOffset = 0;
        targetVisualOffset = graphics.getTileSize()/2;
        speedVisualOffset = (float)(targetVisualOffset) / GamePanelGraphics.ANIMATION_CYCLE;
        state = PULSATING;
    }

    private void startAnimationCycle() {
        animationFramesLeft = GamePanelGraphics.ANIMATION_CYCLE;
    }

}
