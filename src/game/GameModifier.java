package game;

public interface GameModifier {

    /**
     * @return NameID of the modifier
     */
    String getNameID();

    /**
     * @return true if modifier can be applied right now, false otherwise
     */
    boolean isApplicable();

    /**
     * Applies the modifier.
     */
    void apply();

    /**
     * @return true if modifier is applied actively (and needs visual cues for the player), false otherwise
     */
    boolean isActive();
}
