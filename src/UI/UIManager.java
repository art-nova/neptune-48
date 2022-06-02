package UI;

import javax.swing.JFrame;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class UIManager {

    private static JFrame frame;
    private static LevelGraphics level;
    
    public static void initFrame() {
	frame = new JFrame("title");
    }
    
    public static void init(){
        level = App.getLevel();
        
        frame.setSize(level.sizes.windowWidth,level.sizes.windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void clearFrame(){
        frame.getContentPane().removeAll();
        frame.repaint();
    }
    
    public static void loadLevel(){
        clearFrame();
        frame.add(level);
    }
    
    public static JFrame getFrame() {
	return frame;
    }
}
