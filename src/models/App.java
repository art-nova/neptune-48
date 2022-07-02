package models;
import UI.LevelsMenu;
import UI.MainMenu;
import data.DataManager;
import data.LevelIdentifier;
import data.PlayerData;
import UI.LevelMenu;
import misc.AudioManager;
import java.awt.Font;
import java.io.File;
/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {

    private static LevelsMenu levels;
    private static MainMenu mainMenu;
    private static LevelMenu level;
    public static Font lexenDeca;
    public static Font rubik;
    public static Font exo2;

    public static void main(String[] args) {
        AudioManager.init();
        try {
            lexenDeca = Font.createFont(0, new File("resources/fonts/LexendDeca-Regular.ttf"));
            rubik = Font.createFont(0, new File("resources/fonts/Rubik-VariableFont_wght.ttf"));
            exo2 = Font.createFont(0, new File("resources/fonts/Exo2-Bold.ttf"));
        } catch (Exception e) {System.out.println("Fonts: " + e.getMessage());}
        
        try {
            DataManager.newPlayerData();
            PlayerData playerData = DataManager.loadPlayerData();
            playerData.unlockAbility("bonusTurns");
            playerData.unlockAbility("resistance");
            playerData.unlockAbility("betterBaseLevel");
            playerData.unlockAbility("cooldownReduction");
            playerData.unlockAbility("swap");
            playerData.unlockAbility("merge");
            playerData.unlockAbility("dispose");
            playerData.unlockAbility("crit");
            playerData.unlockAbility("safeAttack");
            playerData.unlockLevel(new LevelIdentifier("normal", 1));
            playerData.unlockLevel(new LevelIdentifier("normal", 2));
            playerData.unlockLevel(new LevelIdentifier("normal", 3));
            playerData.unlockLevel(new LevelIdentifier("normal", 4));
            playerData.unlockLevel(new LevelIdentifier("normal", 5));
            playerData.setLevelBestResult(new LevelIdentifier("normal", 0), 250, 3);
            playerData.setLevelBestResult(new LevelIdentifier("normal", 1), 100, 2);
            playerData.setLevelBestResult(new LevelIdentifier("normal", 2), 10, 1);
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

    public static void loadLevelFromLevels(LevelIdentifier levelIdentifier){
        level = new LevelMenu(levelIdentifier);
        level.setLocation(levels.getLocation());
        levels.dispose();
    }

    public static void loadLevelFromLevel(LevelIdentifier levelIdentifier){
        var position = level.getLocation();
        LevelMenu level2 = new LevelMenu(levelIdentifier);
        level2.setLocation(position);
        level.dispose();
        level = level2;        
    }

    public static void loadLevelsMenuFromLevels(String hardness){
        var position = levels.getLocation();
        LevelsMenu levels2 = new LevelsMenu(hardness);
        levels2.setLocation(position);
        levels.dispose();
        levels = levels2;  
    }

    public static void loadLevelsFromLevel(){
        levels = new LevelsMenu("app");
        levels.setLocation(level.getLocation());
        level.dispose();
    }

    public static LevelsMenu getLevelsMenu(){
        return levels;
    }

    /**
     * @return LevelMenu 
     */
    public static LevelMenu getLevelMenu(){
        return level;
    }
}
