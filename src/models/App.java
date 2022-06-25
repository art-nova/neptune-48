package models;
import UI.LevelsMenu;
import UI.MainMenu;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {
     
    private static LevelsMenu lm;

    
    public static void main(String[] args) {
        //MainMenu m  = new MainMenu();
        lm = new LevelsMenu();
        
    }

    public static LevelsMenu getLevelsMenu(){
        return lm;
    }
}
