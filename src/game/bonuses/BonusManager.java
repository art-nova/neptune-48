package game.bonuses;

import game.GamePanel;

import java.util.Set;

public class BonusManager {

    public final Set<String> unlockedBonuses;

    public BonusManager(Set<String> unlockedBonuses) {
        this.unlockedBonuses = unlockedBonuses;
    }

    /**
     * Creates a new bonus from its NameID and supplies given GamePanel as its target.
     *
     * @param nameID bonus NameID
     * @param gp GamePanel to interact with
     * @return newly instantiated bonus of corresponding class, cast to parent class Bonus.
     */
    public static Bonus newBonusByNameID(String nameID, GamePanel gp) {
        IllegalArgumentException badBonus = new IllegalArgumentException("Could not find bonus!");
        if (nameID == null || nameID.isEmpty()) throw badBonus;
        try {
            String className = "game.bonuses."+Character.toUpperCase(nameID.charAt(0))+nameID.substring(1);
            return (Bonus)Class.forName(className).getConstructor(GamePanel.class).newInstance(gp);
        } catch (Exception e) {
            throw badBonus;
        }
    }
}
