package models;
import UI.LevelsMenu;
import UI.MainMenu;
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
        mainMenu  = new MainMenu();
    }

    public static void loadLevelsMenu(){
        levels = new LevelsMenu();
        levels.setLocation(mainMenu.getLocation());
        mainMenu.dispose();
    }

    public static void loadLevel(){
        level = new LevelMenu();
        level.setLocation(levels.getLocation());
        levels.dispose();
    }

    public static LevelsMenu getLevelsMenu(){
        return levels;
    }
}
