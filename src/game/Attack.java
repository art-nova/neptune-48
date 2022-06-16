package game;

import game.events.*;
import game.gameobjects.Board;
import game.gameobjects.BoardCell;
import game.gameobjects.Entity;
import game.gameobjects.Tile;
import game.utils.GamePanelGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages all attacks in the level.
 *
 * @author Artem Novak
 */
public class Attack extends GameModifier {
    /**
     * Represents the number of turns the attack remains locked after usage before becoming usable again.
     */
    public static int COOLDOWN = 10;

    private final Board board;
    private final Entity entity;
    private final KeyHandler keyHandler;
    private final Point targetPoint;
    private final ArrayList<AttackListener> attackListeners = new ArrayList<>();
    private int currentCooldown = 0;

    public Attack(GamePanel gp) {
        super(gp);
        this.board = gp.getBoard();
        this.entity = gp.getEntity();
        this.keyHandler = gp.getKeyHandler();
        gp.getBoard().addTurnListener(() -> {
            if (currentCooldown > 0) {
                currentCooldown--;
                if (currentCooldown == 0) setState(GameModifier.APPLICABLE); // Triggers UI data listeners as well
                else {
                    for (UIDataListener listener : new ArrayList<>(uiDataListeners)) listener.onUIDataChanged();
                }
            }
        });
        int targetX = (int)(gp.getEntity().getX() + (GamePanelGraphics.ENTITY_WIDTH - GamePanelGraphics.TILE_SIZE)/2f);
        int targetY = (int)(gp.getEntity().getY() + (GamePanelGraphics.ENTITY_HEIGHT - GamePanelGraphics.TILE_SIZE)/2f);
        targetPoint = new Point(targetX, targetY);
    }

    @Override
    public String getNameID() {
        return "attack";
    }

    /**
     * Starts selection of attack origin cell. Does nothing if the state of this attack is not {@link #APPLICABLE}.
     */
    @Override
    public void startApplication() {
        if (state == APPLICABLE) {
            board.initSelection(x -> board.getTileInCell(x) != null, 1);
            setState(APPLYING);
            board.addCellSelectionListener(new CellSelectionListener() {
                @Override
                public void onSelectionCompleted(List<BoardCell> cells) {
                    startAttack(cells.get(0));
                    board.removeCellSelectionListener(this);
                }

                @Override
                public void onSelectionAborted() {
                    board.removeCellSelectionListener(this);
                }
            });
        }
    }

    /**
     * Updates logical state of the attack.
     */
    public void update() {
        if (keyHandler.isKeyPressed() && keyHandler.getLastPressKey().equals("space")) startApplication();
    }

    /**
     * Starts attack from given cell.
     * <br>
     * Note: this is not the method for standard attack (see {@link #startApplication()} for that).
     * This method intentionally does not respect applicability to allow for programmatic overattack.
     * Use this only if you know what you are doing!
     *
     * @param cell cell to start attack from
     * @throws GameLogicException when trying to attack from empty cell
     */
    public void startAttack(BoardCell cell) throws GameLogicException {
        Tile tile = board.getTileInCell(cell);
        if (tile == null) throw new GameLogicException("Trying to attack from empty cell");
        board.moveCellContentTransient(cell, targetPoint);
        AttackEvent attackEvent = new AttackEvent(cell, tile, gp.getBaseTileDamage());
        currentCooldown = COOLDOWN;
        setState(GameModifier.UNAPPLICABLE);
        for (AttackListener listener : attackListeners) listener.onAttack(attackEvent); // Attack event may potentially be modified.
        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    entity.changeHealth(-attackEvent.getDamage());
                    // TODO code that will play a visual explosion in a later release
                    board.removeStateListener(this);
                }
            }
        });
    }

    /**
     * Returns the current cooldown of the attack.
     *
     * @return number of turns before attack becomes available (aka "cooldown")
     */
    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void addAttackListener(AttackListener listener) {
        attackListeners.add(listener);
    }

    public void removeAttackListener(AttackListener listener) {
        attackListeners.remove(listener);
    }
}
