package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class Parallax extends JPanel{


    public Parallax() {
        super();
        setSize(1000, 700);
        setBackground(Color.LIGHT_GRAY);
        
        JLayeredPane pane = new JLayeredPane();
        pane.setBackground(Color.BLACK);
        pane.setPreferredSize(new Dimension(1000, 700));
        List<Layer> layers = new ArrayList<>(); 
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int perX = e.getX() * 100 / (getWidth()) ;
                int perY = e.getY() * 100 / (getHeight());
                
                layers.get(0).setLocationByPercent(perX, perY);
                layers.get(2).setLocationByPercent(perX, perY);
                layers.get(1).setLocationByPercent(perX, perY);
                
            }
        });
        try {
            
            Layer layer = new Layer(363,142,  345,375,11,38, new ImageIcon(ImageIO.read(new File("resources/images/cloudBack.png"))));
            layers.add(layer);
            pane.add(layer,3);
            
            layer = new Layer(796,603,  -35,205,80,100, new ImageIcon(ImageIO.read(new File("resources/images/mountain.png"))));
            layers.add(layer);                
            pane.add(layer, 0);
            
            layer = new Layer(452,196,  48,140,55,97, new ImageIcon(ImageIO.read(new File("resources/images/cloudFront.png"))));
            layers.add(layer);
            pane.add(layer, 1);
            
            
            
        } catch (Exception e) {
            System.out.println("parallax " + e);
        }
        add(pane);
    }


    private class Layer extends JPanel{
        int minX, maxX, minY, maxY;
        public void setLocationByPercent(int percentX, int percentY){
            int x = minX + percentX * (maxX - minX) / 100;
            int y = minY + percentY * (maxY - minY) / 100;
            setLocation(x,y);
        }

        public Layer(int width, int height, int minX, int maxX, int minY, int maxY, ImageIcon image){
            super();
            setBounds(minX,minY,width, height);
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            JLabel label = new JLabel(image);
            setBackground(new Color(0,0,0,0));
            add(label);
            setVisible(true);
        }
    }
}
