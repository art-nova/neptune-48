package models;
import UI.LevelsMenu;
import UI.MainMenu;
import UI.LevelMenu;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {
     
    private static LevelsMenu lm;
    private static MainMenu mainMenu;

    public static void main(String[] args) {
        mainMenu  = new MainMenu();
        //lm = new LevelsMenu();
        //LevelMenu level = new LevelMenu();
        
    }

    public static void loadLevelsMenu(){
        lm = new LevelsMenu();
        lm.setLocation(mainMenu.getLocation());
        mainMenu.dispose();
    }

    public static LevelsMenu getLevelsMenu(){
        return lm;
    }
}
