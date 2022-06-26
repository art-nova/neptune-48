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

    
    public static void main(String[] args) {
        //MainMenu m  = new MainMenu();
        lm = new LevelsMenu();
        //LevelMenu level = new LevelMenu();
        
    }

    public static LevelsMenu getLevelsMenu(){
        return lm;
    }
}
