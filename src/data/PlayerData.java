package data;

import game.GameLogicException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds dynamic data specific to a player profile, such as unlocked levels, level scores, abilities etc.
 * <br>
 * After permanent changes an instance needs to be serialized separately via {@link DataManager#savePlayerData(PlayerData)}.
 *
 * @author Artem Novak
 */
public class PlayerData implements Serializable {
    private final Map<String, Map<Integer, DynamicLevelEntry>> unlockedLevels = new HashMap<>();
    private final List<String> unlockedAbilities = new ArrayList<>();

    private String activeAbility1;
    private String activeAbility2;
    private String passiveAbility;

    /**
     * Constructs a new PlayerData object and unlocks the first level of "normal" difficulty.
     */
    public PlayerData() {
        unlockedLevels.put("normal", new HashMap<>());
        unlockedLevels.put("hard", new HashMap<>());
        unlockLevel(new LevelIdentifier("normal", 0));
    }

    /**
     * Class that holds dynamic (player-based) information about a level
     */
    public record DynamicLevelEntry (int bestTurnsLeft, int stars) implements Serializable {
        public DynamicLevelEntry {
            if (bestTurnsLeft < 0) throw new GameLogicException("Illegal turns left: " + bestTurnsLeft);
            if (stars < 1 || stars > 3) throw new GameLogicException("Illegal number of stars: " + stars);
        }
    }

    public boolean isHardModeUnlocked() {
        return !unlockedLevels.get("hard").isEmpty();
    }

    public boolean isLevelUnlocked(LevelIdentifier level) {
        return unlockedLevels.get(level.difficulty()).containsKey(level.index());
    }

    /**
     * Checks if a level is completed.
     *
     * @param level level identifier
     * @return true if it is unlocked and has a non-null best result entry
     */
    public boolean isLevelCompleted(LevelIdentifier level) {
        return isLevelUnlocked(level) && unlockedLevels.get(level.difficulty()).get(level.index()) != null;
    }

    /**
     * Puts level on the list of unlocked levels as if it was newly unlocked.
     *
     * @param level identifier of the level
     * @throws GameLogicException if the level is already unlocked
     */
    public void unlockLevel(LevelIdentifier level) throws GameLogicException {
        if (isLevelUnlocked(level)) throw new GameLogicException("Trying to unlock an already unlocked level");
        unlockedLevels.get(level.difficulty()).put(level.index(), null);
    }

    /**
     * Removes the level from the list of unlocked levels, erasing its best result. Does not remove the unlocked ability.
     * <br>
     * Not intended for normal game usage.
     *
     * @param level level identifier
     */
    public void clearUnlockedLevel(LevelIdentifier level) {
        unlockedLevels.get(level.difficulty()).remove(level.index());
    }

    /**
     * Sets a new best result for an unlocked level (overriding the previous result).
     *
     * @param level level identifier of the level
     * @param timeLeft time that was left until losing when player won
     * @param stars number of stars the player earned
     */
    public void setLevelBestResult(LevelIdentifier level, int timeLeft, int stars) {
        if (!isLevelUnlocked(level)) throw new GameLogicException("Trying to change best result of a non-unlocked (locked or nonexistent) level " + level);
        unlockedLevels.get(level.difficulty()).put(level.index(), new DynamicLevelEntry(timeLeft, stars));
    }

    /**
     * Returns the highest recorded number of turns left after this level's completion.
     *
     * @param level level identifier
     * @return the turns spent
     */
    public int getLevelTurnsLeft(LevelIdentifier level) {
        if (!isLevelCompleted(level)) throw new GameLogicException("Trying to query results of an uncompleted level");
        return unlockedLevels.get(level.difficulty()).get(level.index()).bestTurnsLeft();
    }

    /**
     * Returns best recorded number of stars for the level.
     *
     * @param level level identifier
     * @return the stars earned, or 0 if the level was not completed yet
     */
    public int getLevelStars(LevelIdentifier level) {
        if (!isLevelCompleted(level)) throw new GameLogicException("Trying to query results of an uncompleted level");
        return unlockedLevels.get(level.difficulty()).get(level.index()).stars();
    }

    /**
     * Register's given ability NameID as one of the unlocked.
     * <br>
     * This method is not responsible for NameID correctness.
     *
     * @param nameID ability's NameID
     */
    public void unlockAbility(String nameID) {
        if (!unlockedAbilities.contains(nameID)) unlockedAbilities.add(nameID);
    }

    /**
     * Removes given ability from the list of unlocked.
     *
     * @param nameID ability NameID
     */
    public void clearUnlockedAbility(String nameID) {
        unlockedAbilities.remove(nameID);
    }

    public boolean isAbilityUnlocked(String nameID) {
        return unlockedAbilities.contains(nameID);
    }

    /**
     * Returns a list of unlocked abilities.
     *
     * @return copy of the internal {@link List} of unlocked ability NameIDs
     */
    public List<String> getUnlockedAbilities() {
        return new ArrayList<>(unlockedAbilities);
    }

    public String getActiveAbility1() {
        return activeAbility1;
    }

    /**
     * Puts the NameID of the given ability into the first active ability slot.
     * <br>
     * This method is not responsible for ability layout correctness.
     *
     * @param activeAbility1 ability's NameID
     */
    public void setActiveAbility1(String activeAbility1) {
        if (!unlockedAbilities.contains(activeAbility1)) throw new GameLogicException("Trying to select an ability that is not unlocked: " + activeAbility1);
        this.activeAbility1 = activeAbility1;
    }
    public String getActiveAbility2() {
        return activeAbility2;
    }
    public void removeActiveAbility1() {
        activeAbility1 = null;
    }

    /**
     * Puts the NameID of the given ability into the second active ability slot.
     * <br>
     * This method is not responsible for ability layout correctness.
     *
     * @param activeAbility2 ability's NameID
     */
    public void setActiveAbility2(String activeAbility2) {
        if (!unlockedAbilities.contains(activeAbility2)) throw new GameLogicException("Trying to select an ability that is not unlocked: " + activeAbility1);
        this.activeAbility2 = activeAbility2;
    }

    public void removeActiveAbility2(){
        activeAbility2 = null;
    }

    public String getPassiveAbility() {
        return passiveAbility;
    }

    /**
     * Puts the NameID of the given ability into the passive ability slot.
     * <br>
     * This method is not responsible for ability layout correctness.
     *
     * @param passiveAbility ability's NameID
     */
    public void setPassiveAbility(String passiveAbility) {
        if (!unlockedAbilities.contains(passiveAbility)) throw new GameLogicException("Trying to select an ability that is not unlocked: " + activeAbility1);
        this.passiveAbility = passiveAbility;
    }

    public void removePassiveAbility() {
        passiveAbility = null;
    }
}
