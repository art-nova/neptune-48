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

    public static void showParallax(){
        frame.setSize(1000,700);
        Parallax parallax = new Parallax(1000, 700);
        frame.add(parallax);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static void init(){
        level = App.getLevel();
        
        frame.setSize(level.sizes.windowWidth,level.sizes.windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setLocationRelativeTo(null);
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
