package game;

/**
 * Class that represents an abstract game modifier - an entity that is not displayed, but influences the game process in some way.
 * <br>
 * As a {@link UIDataHolder}, triggers corresponding event when modifier state is changed.
 *
 * @author Artem Novak
 */
public abstract class GameModifier {
    // States
    public static final int APPLICABLE = 0, UNAPPLICABLE = 1, APPLYING = 2;

    protected GamePanel gp;
    protected int state;

    public GameModifier(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * @return NameID of the modifier
     */
    public abstract String getNameID();

    /**
     * Starts application of this modifier. Depending on the modifier, it may or may not produce effect immediately.
     */
    public abstract void startApplication();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0 || state > 2) throw new IllegalArgumentException(getClass().getName() + " does not support state " + state);
        this.state = state;
    }

    protected abstract boolean determineApplicability();

    /**
     * Is meant to be run every time one of the applicability components changes, to determine whether that results in general applicability change.
     */
    public void updateApplicability() {
        if (determineApplicability()) setState(APPLICABLE);
        else setState(UNAPPLICABLE);
    }
}
