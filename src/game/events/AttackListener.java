package game.events;

/**
 * Interface that allows to register attacks.
 *
 * @author Artem Novak
 */
public interface AttackListener {
    /**
     * Is triggered when attack processing starts (damage to entity is not yet applied).
     *
     * @param e attack event
     */
    void onAttack(AttackEvent e);
}
