package game.events;

import game.BoardCell;
import game.Tile;

/**
 * Class that holds information about an attack that is about to or has occurred.
 *
 * @author Artem Novak
 */
public class AttackEvent {
    private int damagePercent = 100;
    private int damageNegation;
    private final BoardCell originCell;
    private final Tile tile;
    private final int baseTileDamage;
    private boolean turnConsuming = true;

    /**
     * Creates a new attack event.
     *
     * @param originCell cell which the tile originally occupied
     * @param tile tile that is used in the attack
     * @param baseTileDamage damage of the level 1 tile
     */
    public AttackEvent(BoardCell originCell, Tile tile, int baseTileDamage) {
        this.originCell = originCell;
        this.tile = tile;
        this.baseTileDamage = baseTileDamage;
    }

    /**
     * @return damage damageScale in percents
     */
    public int getDamagePercent() {
        return damagePercent;
    }

    /**
     * Changes current damage scale by given percentage.
     * For example, if the damageScale is set to 150% and offset is 50%, resulting damage scale will be 200%.
     *
     * @param percentageOffset offset in percents (is added to the damageScale)
     */
    public void offsetDamagePercent(int percentageOffset) {
        damagePercent += percentageOffset;
        if (damagePercent < 0) damagePercent = 0;
    }

    /**
     * Returns the percentage of damage that gets ignored, on a scale from 0% to 100%.
     *
     * @return damage negation percentage
     */
    public int getDamageNegation() {
        return damageNegation;
    }

    /**
     * Changes current damage negation (from the attack target).
     * Damage negation only works on a scale from 0% to 100%, 0% not affecting damage calculation and 100% resulting in damage 0.
     *
     * @param damageNegationOffset offset in percents
     */
    public void offsetDamageNegation(int damageNegationOffset) {
        damageNegation += damageNegationOffset;
        if (damageNegation > 100) damageNegation = 100;
        if (damageNegation < 0) damageNegation = 0;
    }

    public long getDamage() {
        return tile.getLevel() == 11 ? Long.MAX_VALUE : Math.round((Math.pow(baseTileDamage, tile.getLevel()) * damagePercent / 100f) * (100 - damageNegation) / 100f);
    }

    /**
     * @return cell which the tile occupied prior to the attack
     */
    public BoardCell getOriginCell() {
        return originCell;
    }

    /**
     * @return object of the tile used in this attack
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * @return whether this attack will trigger a turn reaction
     */
    public boolean isTurnConsuming() {
        return turnConsuming;
    }

    /**
     * @param turnConsuming whether this attack will trigger a turn reaction
     */
    public void setTurnConsuming(boolean turnConsuming) {
        this.turnConsuming = turnConsuming;
    }
}
