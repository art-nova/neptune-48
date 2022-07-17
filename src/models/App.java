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
            lexenDeca = Font.createFont(0, App.class.getResourceAsStream("/fonts/LexendDeca-Regular.ttf"));
            rubik = Font.createFont(0, App.class.getResourceAsStream("/fonts/Rubik-VariableFont_wght.ttf"));
            exo2 = Font.createFont(0, App.class.getResourceAsStream("/fonts/Exo2-Bold.ttf"));
        } catch (Exception e) {System.out.println("Fonts: " + e.getMessage());}

        mainMenu  = new MainMenu();
        AudioManager.setBG("menu");
        AudioManager.playBG();
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
        AudioManager.setBG("menu");
        AudioManager.playBG();
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
