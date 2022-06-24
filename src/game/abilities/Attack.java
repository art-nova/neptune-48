package game.abilities;

import game.GameLogicException;
import game.GamePanel;
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
 * Class that implements the attacking feature.
 *
 * @author Artem Novak
 */
public class Attack extends ActiveAbility {
    /**
     * Represents the number of turns the attack remains locked after usage before becoming usable again.
     */
    public static int DEFAULT_COOLDOWN = 10;

    private final Board board;
    private final Entity entity;
    private final Point targetPoint;
    private final ArrayList<AttackListener> attackListeners = new ArrayList<>();

    public Attack(GamePanel gp, AbilityManager abilityManager) {
        super(gp, abilityManager, DEFAULT_COOLDOWN);
        this.board = gp.getBoard();
        this.entity = gp.getEntity();
        GamePanelGraphics graphics = gp.getGameGraphics();
        int targetX = (int)(gp.getEntity().getX() + (graphics.getEntityWidth() - graphics.getTileSize())/2f);
        int targetY = (int)(gp.getEntity().getY() + (graphics.getEntityHeight() - graphics.getTileSize())/2f);
        targetPoint = new Point(targetX, targetY);
    }

    @Override
    public String getNameID() {
        return "attack";
    }

    /**
     * Starts selection of attack origin cell with subsequent attack if selection is successful.
     */
    @Override
    public void startApplication() {
        super.startApplication();
        board.initSelection(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && !tile.isLocked();
        }, 1);
        setState(APPLYING);
        board.addCellSelectionListener(new CellSelectionListener() {
            @Override
            public void onSelectionUpdated(List<BoardCell> cells) {

            }

            @Override
            public void onSelectionCompleted(List<BoardCell> cells) {
                board.removeCellSelectionListener(this);
                setCurrentCooldown(cooldown);
                startAttack(cells.get(0));
                for (AbilityListener listener : new ArrayList<>(abilityListeners)) listener.onAbilityApplied();
            }

            @Override
            public void onSelectionAborted() {
                board.removeCellSelectionListener(this);
                updateApplicability();
            }
        });
    }

    /**
     * Starts attack from given cell.
     *
     * @param cell cell to start attack from
     * @throws GameLogicException when trying to attack from empty cell
     */
    private void startAttack(BoardCell cell) throws GameLogicException {
        super.startApplication();
        Tile tile = board.getTileInCell(cell);
        if (tile == null) throw new GameLogicException("Trying to attack from empty cell");
        board.disposeCellContent(cell);
        board.animateTileMoveTransient(tile, targetPoint);
        AttackEvent attackEvent = new AttackEvent(cell, tile, gp.getBaseTileDamage());
        board.addStateListener(new StateListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                if (oldState == Board.ANIMATING && newState == Board.IDLE) {
                    board.removeStateListener(this);
                    if (gp.getGameMode() == GamePanel.GAME_MODE_ATTACK) entity.animateDamage(attackEvent.getDamage());
                    else entity.animateHealing(attackEvent.getDamage());
                }
            }
        });
        for (AttackListener listener : new ArrayList<>(attackListeners)) listener.onAttack(attackEvent); // Attack event may potentially be modified.
        if (gp.getGameMode() == GamePanel.GAME_MODE_ATTACK) entity.takeDamage(attackEvent.getDamage());
        else entity.takeHealing(attackEvent.getDamage());
        board.setState(Board.ANIMATING);
    }

    @Override
    protected boolean determineApplicability() {
        return super.determineApplicability() && !board.getCellsByPredicate(x -> {
            Tile tile = board.getTileInCell(x);
            return tile != null && !tile.isLocked();
        }).isEmpty();
    }

    public void addAttackListener(AttackListener listener) {
        attackListeners.add(listener);
    }

    public void removeAttackListener(AttackListener listener) {
        attackListeners.remove(listener);
    }
}
