package models;
import UI.LevelGraphics;
import UI.LevelsMenu;
import UI.MainMenu;
import UI.UIManager;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {
     
    private static LevelGraphics levelGraphics;
    private static LevelsMenu lm;
    
    public static LevelGraphics getLevel(){
        return levelGraphics;
    }
    
    public static void main(String[] args) {
        //MainMenu m  = new MainMenu();
        lm = new LevelsMenu();
        /*
        UIManager.initFrame();
        
        //UIManager.showParallax();
        
	    
        levelGraphics = new LevelGraphics();
        levelGraphics.addGraphics();
        UIManager.init();
        UIManager.loadLevel();
        */
    }

    public static LevelsMenu getLevelsMenu(){
        return lm;
    }
}
