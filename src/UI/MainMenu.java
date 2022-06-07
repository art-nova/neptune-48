package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main menu JFrame
 * @author Artemii Kolomiichuk
 */
public class MainMenu extends JFrame{
    int width, height;
    MouseAdapter moveAdapter;
    
    public MainMenu() {
        super("Menu");
        this.width = 800;
        this.height = 925;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(width, height));
        List<Layer> layers = new ArrayList<>(); 
        
        moveAdapter = new MouseAdapter() {  
            @Override
            public void mouseMoved(MouseEvent e) {                
                Component component = e.getComponent();
                Point pt = new Point();
                pt.y -= e.getYOnScreen() + component.getLocation().y;
                pt.x -= e.getXOnScreen() + component.getLocation().x;         
                SwingUtilities.convertPointToScreen(pt, component);
                
                int perX = Math.abs(pt.x) * 100 / (width) ;
                int perY = Math.abs(pt.y) * 100 / (height);
                for (Layer layer : layers) {
                    layer.setLocationByPercent(perX, perY);
                }
                revalidate();
                repaint();
            }
        };
        
        
        /* Layers:
        * 00     background 
        * 01     cloud4
        * 02     cloud3
        * 03     mount3
        * 04     mount2
        * 05     mount1
        * 06     grassTop
        * 07     dirt
        * 08     stones
        * 09     stoneButton
        * 10     grass         
        * 11     bush
        * 12     bushes
        * 13     cloud5
        * 14     cloud2
        * 15     cloud1
        */
        try {
            String p = "resources/images/mainMenu/";
            
            Layer layer = new Layer(0,0,0,0, new ImageIcon(ImageIO.read(new File(p + "background.png"))));
            layers.add(layer);
            pane.add(layer,100);

            layer = new Layer(159,203,1,14, new ImageIcon(ImageIO.read(new File(p + "cloud4.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-24,53,12,36, new ImageIcon(ImageIO.read(new File(p + "cloud3.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(144,151,106,111, new ImageIcon(ImageIO.read(new File(p + "mount3.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(433,450,41,48, new ImageIcon(ImageIO.read(new File(p + "mount2.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-188,-148,86,98, new ImageIcon(ImageIO.read(new File(p + "mount1.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-7,-7,655,655, new ImageIcon(ImageIO.read(new File(p + "grassTop.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-7,-7,727,727, new ImageIcon(ImageIO.read(new File(p + "dirt.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(0,5,765,765, new ImageIcon(ImageIO.read(new File(p + "stones.png"))));
            layers.add(layer);                
            pane.add(layer, 0);



            ImageIcon stoneStill = new ImageIcon(ImageIO.read(new File(p + "stoneButton.png")));
            ImageIcon stoneCovered = new ImageIcon(ImageIO.read(new File(p + "stoneButtonCover.png")));
            Layer layerStoneButton = new Layer(652,657,770,770, stoneStill);
            layers.add(layerStoneButton);
            pane.add(layerStoneButton, 1);

            layerStoneButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    layerStoneButton.setImage(stoneCovered);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    layerStoneButton.setImage(stoneStill);
                    revalidate();
                    repaint();
                }
            });
            layerStoneButton.addMouseMotionListener(moveAdapter);



            layer = new Layer(-360,-355,717,717, new ImageIcon(ImageIO.read(new File(p + "grass.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(250,310,505,520, new ImageIcon(ImageIO.read(new File(p + "bush.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-102,3,477,498, new ImageIcon(ImageIO.read(new File(p + "bushes.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(595,620,7,16, new ImageIcon(ImageIO.read(new File(p + "cloud5.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(288,420,12,42, new ImageIcon(ImageIO.read(new File(p + "cloud2.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-20,185,25,72, new ImageIcon(ImageIO.read(new File(p + "cloud1.png"))));
            layers.add(layer);                
            pane.add(layer, 0);
            
        } catch (Exception e) {
            System.out.println("main menu error: " + e);
        }
        add(pane);
        pane.addMouseMotionListener(moveAdapter);
        setVisible(true);
    }

    private class Layer extends JPanel{
        private int minX, maxX, minY, maxY;
        private JLabel label;

        public void setLocationByPercent(int percentX, int percentY){
            int x = minX + percentX * (maxX - minX) / 100;
            int y = minY + percentY * (maxY - minY) / 100;
            setLocation(x,y);
        }

        public Layer(int minX, int maxX, int minY, int maxY, ImageIcon image){
            super();
            setBounds(minX, minY, image.getIconWidth(), image.getIconHeight()+10);
            this.minX = minX;
            this.maxX = minX + (int)((maxX - minX)/1.4);
            this.minY = minY;
            this.maxY = minY + ((maxY - minY)/5);
            label = new JLabel();
            label.setIcon(image);
            setBackground(new Color(0,0,0,0));
            add(label);
            setVisible(true);
        }

        public void setImage(ImageIcon image){
            label.setIcon(image);
        }
    }
}
