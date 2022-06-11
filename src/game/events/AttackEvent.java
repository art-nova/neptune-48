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
    private final BoardCell originCell;
    private final Tile tile;
    private final int baseTileDamage;
    private boolean consumesTurn = true;

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
    }

    public int getDamage() {
        return Math.round(baseTileDamage * damagePercent / 100f);
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
    public boolean getConsumesTurn() {
        return consumesTurn;
    }

    /**
     * @param consumesTurn whether this attack will trigger a turn reaction
     */
    public void setConsumesTurn(boolean consumesTurn) {
        this.consumesTurn = consumesTurn;
    }
}
