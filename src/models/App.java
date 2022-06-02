package models;
import UI.fragments.FilledBox;
import UI.HighlightedButton;
import UI.fragments.MovingTile;
import Legacy.TileField;
import UI.LevelGraphics;
import UI.UIManager;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;


import javax.imageio.ImageIO;

/**
 * Main class of the game.
 * @author Artemii Kolomiichuk
 */
public class App {

    /**
     * Path to the programm folder.
     */
//    public final static String PATH = "C:/Users/temak/Desktop/2048";
    static TileField field;
    static JLabel label;
    static JFrame frame;
    static JPanel gradient;
    static ArrayList<MovingTile> tiles = new ArrayList<MovingTile>();
    static ArrayList<Integer> bounds = new ArrayList<Integer>();
    static ArrayList<Integer> targets = new ArrayList<Integer>();
    static int counter = 0;

    
    
    private static LevelGraphics levelGraphics;
    
    public static LevelGraphics getLevel(){
        return levelGraphics;
    }
    

    
    public static void main(String[] args) {
        //ImageSet set = new ImageSet("neptun", 75, 50);
	UIManager.initFrame();
        levelGraphics = new LevelGraphics();
        levelGraphics.addGraphics();
        UIManager.init();
        UIManager.loadLevel();
    }

    static void moveTiles(){
        if(counter%2 == 0){
            field.moveToRightRigid();
        }
        else{
            field.moveToLeftRigid();
        }
        for (MovingTile tile : tiles) {
            tile.setRelativeSpeed(7.7);
        }
        counter++;
        System.out.println(field);
    }
    
    static JLayeredPane tiles(){
        JLayeredPane pane = new JLayeredPane();
        field = new TileField(6, 6, 75, 5, 200, 200);
        //panel.setLayout();
        pane.setBounds(0,0,1200,1200);
        bounds = new ArrayList<Integer>(Arrays.asList( 280,200,360,200,360,280,600,280,280,360,520,360,200,440,200,520,520,520,360,600,440,600,600,600));
        targets = new ArrayList<Integer>(Arrays.asList(520,200,600,200,520,280,600,280,520,360,600,360,600,440,520,520,600,520,440,600,520,600,600,600));
        for(int i = 0; i < 12; i++){
            Image image = null;
            try {
                //                           ->path                 <-
                image = (ImageIO.read(new File("/paletteBlue.png")));
                image = image.getScaledInstance(75, 75, Image.SCALE_DEFAULT);
            } catch(Exception e) {System.out.println("invalid image path");}
            
            MovingTile tile = new MovingTile(new ImageIcon(image), new Point(bounds.get(i*2), bounds.get(i*2 + 1)));
            tile.x = bounds.get(i*2);
            tile.y = bounds.get(i*2 + 1);
            tile.setBounds(bounds.get(i*2), bounds.get(i*2 + 1), 75, 75);
            tile.targetPoint = new Point(targets.get(i*2), targets.get(i*2 + 1));
            
            tiles.add(tile);
            
            int row = ((int)tile.y - 200) % 79;
            
            int collumn = ((int)tile.x - 200) % 79;
            
            tile.positionOnField = row* field.collumns + collumn;
            
            field.tiles[tile.positionOnField] = tile;
            pane.add(tile, 10);
        }
        
        FilledBox box = new FilledBox(Color.yellow);
        box.setBounds(46,50,2000,1700);
        //pane.add(box, 1000);
        JLabel label = new JLabel("abba");
        //label.setBounds(0, 0, 100, 200);
        label.setFont(new Font("Rubik", Font.PLAIN, 65));
        label.setForeground(Color.white);
        
        
        HighlightedButton button = new HighlightedButton(270,80, new Color(0,87,146), new Color(0,187,240, 200), label);
        button.setBounds(30,70,270,80);
        pane.add(button);
        button.gradient.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                   moveTiles();
        }});
        pane.setBackground(new Color(0,0,0,0));
        return pane;
    }
    
    static JLayeredPane gradients(){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0, 0, 500,200);
        /*
        try {
            int startX = 100;
            int startY = 600;
            for(int i = 0; i < 3; i++){
                int localI = i;
                int x = startX + i*50;
                int y = startY - i*80;
                Color color1 = UIutilities.randomColor();
                Color color2 = UIutilities.randomColor();
                MoveableGradientBox gradient = 
                    new MoveableGradientBox(600,220,
                            color1,
                            color2, 
                            GradientBox.directions.RIGHT_TO_LEFT,
                            new Point(x,y),
                            frame);
                
                gradient.setBounds(x, y, 350,120);
                
                gradient.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        gradient.targetPoint = new Point(x,y-70); 
                }});

                gradient.addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        gradient.targetPoint = new Point(x,y); 
                }});
                
                
                pane.add(gradient, 100 + i);
                gradient.setTimer();
                gradient.x = x;
                gradient.y = y;
            }
        } catch (Exception e) {}
*/
        
        //pane.setBackground(new Color(0,0,0,0));
        return pane;
    }
}
