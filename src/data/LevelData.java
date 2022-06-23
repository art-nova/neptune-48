package data;

import game.utils.GamePanelGraphics;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Class that holds static information about levels, such as time limit, obstacles etc.
 * <br>
 * Stored information is editable, but is not meant to be edited during normal game runtime.
 *
 * @author Artem Novak
 */
public class LevelData implements Serializable {
    private LevelIdentifier levelIdentifier;
    private LevelIdentifier nextLevelIdentifier;
    private int entityHealth;
    private int time;
    private int twoStarThreshold;
    private int threeStarThreshold;
    private int gameMode;
    private Map<String, Integer> obstacleWeights;
    private int minObstacleInterval;
    private int maxObstacleInterval;
    private String rewardAbility;

    /**
     * Creates a new LevelData object with specified level characteristics.
     *
     * @param levelIdentifier identifier of this level (difficulty and index)
     * @param nextLevelIdentifier identifier of the level unlocked after beating this one (difficulty and index)
     * @param entityHealth max health of the {@link game.gameobjects.Entity}
     * @param time maximal time for the level (in seconds)
     * @param twoStarThreshold maximal spent time necessary to obtain two stars for the level (and the reward ability)
     * @param threeStarThreshold maximal spent time necessary to obtain three stars for the level
     * @param gameMode one of {@link game.GamePanel#GAME_MODE_ATTACK} and {{@link game.GamePanel#GAME_MODE_REPAIR}}
     * @param obstacleWeights map of obstacle NameIDs to their relative occurrence weights
     * @param minObstacleInterval minimal interval in turns between two consecutive obstacles
     * @param maxObstacleInterval maximal interval in turns between two consecutive obstacles
     * @param rewardAbility NameID of the ability awarded to the player upon getting 2 or more stars on this level
     */
    public LevelData(LevelIdentifier levelIdentifier, LevelIdentifier nextLevelIdentifier, int entityHealth, int time, int twoStarThreshold, int threeStarThreshold, int gameMode, Map<String, Integer> obstacleWeights, int minObstacleInterval, int maxObstacleInterval, String rewardAbility) {
        if (levelIdentifier == null) throw new IllegalArgumentException("Unacceptable level identifier: null");
        this.levelIdentifier = levelIdentifier;
        this.nextLevelIdentifier = nextLevelIdentifier;
        this.entityHealth = entityHealth;
        this.time = time;
        this.twoStarThreshold = twoStarThreshold;
        this.threeStarThreshold = threeStarThreshold;
        this.gameMode = gameMode;
        this.obstacleWeights = obstacleWeights;
        this.minObstacleInterval = minObstacleInterval;
        this.maxObstacleInterval = maxObstacleInterval;
        this.rewardAbility = rewardAbility;
    }

    /**
     * Generates a game graphics object based on level's information.
     *
     * @return new {@link GamePanelGraphics} object
     * @throws IOException if any of the images failed to load
     */
    public GamePanelGraphics generateGraphics() throws IOException {
        int tileSize;
        int tileOffset;
        int boardSize;
        if (levelIdentifier.difficulty().equals("normal")) {
            tileSize = 94;
            tileOffset = 5;
        }
        else {
            tileSize = 115;
            tileOffset = 8;
        }
        return new GamePanelGraphics(tileSize, tileOffset, getBoardSize(), getBoardSize(), gameMode, levelIdentifier.index());
    }

    public LevelIdentifier getLevelIdentifier() {
        return levelIdentifier;
    }

    public LevelIdentifier getNextLevelIdentifier() {
        return nextLevelIdentifier;
    }

    /**
     * Finds number of cells in one row or column of the board.
     *
     * @return 5 if difficulty is "normal" and 4 otherwise
     */
    public int getBoardSize() {
        return levelIdentifier.difficulty().equals("normal") ? 5 : 4;
    }

    public int getEntityHealth() {
        return entityHealth;
    }

    public int getTime() {
        return time;
    }

    public int getTwoStarThreshold() {
        return twoStarThreshold;
    }

    public int getThreeStarThreshold() {
        return threeStarThreshold;
    }

    public int getGameMode() {
        return gameMode;
    }

    public Map<String, Integer> getObstacleWeights() {
        return obstacleWeights;
    }

    public int getMinObstacleInterval() {
        return minObstacleInterval;
    }

    public int getMaxObstacleInterval() {
        return maxObstacleInterval;
    }

    /**
     * Finds damage a level 1 tile does (x in the x^lvl tile damage formula).
     *
     * @return 3 if difficulty is "normal" and 4 otherwise
     */
    public int getBaseTileDamage() {
        return levelIdentifier.difficulty().equals("normal") ? 3 : 4;
    }

    public String getRewardAbility() {
        return rewardAbility;
    }

    public void setLevelIdentifier(LevelIdentifier levelIdentifier) {
        this.levelIdentifier = levelIdentifier;
    }

    public void setNextLevelIdentifier(LevelIdentifier nextLevelIdentifier) {
        this.nextLevelIdentifier = nextLevelIdentifier;
    }

    public void setEntityHealth(int entityHealth) {
        this.entityHealth = entityHealth;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTwoStarThreshold(int twoStarThreshold) {
        this.twoStarThreshold = twoStarThreshold;
    }

    public void setThreeStarThreshold(int threeStarThreshold) {
        this.threeStarThreshold = threeStarThreshold;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public void setObstacleWeights(Map<String, Integer> obstacleWeights) {
        this.obstacleWeights = obstacleWeights;
    }

    public void setMinObstacleInterval(int minObstacleInterval) {
        this.minObstacleInterval = minObstacleInterval;
    }

    public void setMaxObstacleInterval(int maxObstacleInterval) {
        this.maxObstacleInterval = maxObstacleInterval;
    }

    public void setRewardAbility(String rewardAbility) {
        this.rewardAbility = rewardAbility;
    }
}
