package models;
import UI.LevelsMenu;
import UI.MainMenu;
import data.DataManager;
import data.PlayerData;
import UI.LevelMenu;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {

    private static LevelsMenu levels;
    private static MainMenu mainMenu;
    private static LevelMenu level;

    public static void main(String[] args) {
        try {
            DataManager.newPlayerData();
            PlayerData playerData = DataManager.loadPlayerData();
            playerData.unlockAbility("bonusTime");
            playerData.unlockAbility("resistance");
            playerData.unlockAbility("betterBaseLevel");
            playerData.unlockAbility("cooldownReduction");
            playerData.unlockAbility("swap");
            playerData.unlockAbility("merge");
            playerData.unlockAbility("dispose");
            playerData.unlockAbility("crit");
            playerData.unlockAbility("safeAttack");
            DataManager.savePlayerData(playerData);
        } catch (Exception e) {
            System.out.println("Error while loading player data:" + e);
        }
        mainMenu  = new MainMenu();
    }

    public static void loadMainMenuFromLevels() {
        mainMenu = new MainMenu();
        mainMenu.setLocation(levels.getLocation());
        levels.dispose();
    }

    public static void loadLevelsMenuFromMain(){
        levels = new LevelsMenu("app");
        levels.setLocation(mainMenu.getLocation());
        mainMenu.dispose();
    }

    public static void loadLevelFromLevels(){
        level = new LevelMenu();
        level.setLocation(levels.getLocation());
        levels.dispose();
    }

    public static void loadLevelsMenuFromLevels(String hardness){
        var position = levels.getLocation();
        levels = new LevelsMenu(hardness);
        levels.setLocation(position);
    }

    public static LevelsMenu getLevelsMenu(){
        return levels;
    }
}
