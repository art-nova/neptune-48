package models;
import UI.LevelGraphics;
import UI.UIManager;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {
     
    private static LevelGraphics levelGraphics;
    
    public static LevelGraphics getLevel(){
        return levelGraphics;
    }
    
    public static void main(String[] args) {
        UIManager.initFrame();
        UIManager.showParallax();
        /* 
	    UIManager.initFrame();
        levelGraphics = new LevelGraphics();
        levelGraphics.addGraphics();
        UIManager.init();
        UIManager.loadLevel();
        */
    }
}
