package UI;

import java.awt.*;
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
import javax.swing.SwingUtilities;

/**
 * Full frame JPanel with Parallax effect
 * @author Artemii Kolomiichuk
 */
public class Parallax extends JPanel{

    int width, height;
    MouseAdapter moveAdapter;
    public Parallax(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        setSize(width, height);
        setBackground(Color.LIGHT_GRAY);
        
        JLayeredPane pane = new JLayeredPane();
        pane.setBackground(Color.BLACK);
        pane.setPreferredSize(new Dimension(width, height));
        List<Layer> layers = new ArrayList<>(); 
        
        moveAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Component component = e.getComponent();
                Point pt = new Point();
                pt.y -= e.getYOnScreen() + component.getLocation().x;
                pt.x -= e.getXOnScreen() + component.getLocation().y;
                SwingUtilities.convertPointToScreen(pt, component);
                
                int perX = Math.abs(pt.x) * 100 / (width) ;
                int perY = Math.abs(pt.y) * 100 / (height);
                
                layers.get(0).setLocationByPercent(perX, perY);
                layers.get(2).setLocationByPercent(perX, perY);
                layers.get(1).setLocationByPercent(perX, perY);
                revalidate();
                repaint();
            }
        };
        
        addMouseMotionListener(moveAdapter);
        try {
            
            Layer layer = new Layer(363,142,  345,375,11,38, new ImageIcon(ImageIO.read(new File("resources/images/cloudBack.png"))));
            layers.add(layer);
            pane.add(layer,3);
            
            layer = new Layer(796,603,  -35,205,80,100, new ImageIcon(ImageIO.read(new File("resources/images/mountain.png"))));
            layers.add(layer);                
            pane.add(layer, 0);
            
            ImageIcon cloudFront = new ImageIcon(ImageIO.read(new File("resources/images/cloudFront.png")));
            ImageIcon cloudOutline = new ImageIcon(ImageIO.read(new File("resources/images/cloudFrontOutline.png")));
            Layer layerF = new Layer(452,196,  48,140,55,97, cloudOutline);
            layers.add(layerF);
            pane.add(layerF, 1);

            layerF.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    layerF.setImage(cloudFront);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    layerF.setImage(cloudOutline);
                }
            });
            
            layerF.addMouseMotionListener(moveAdapter);
            
            
        } catch (Exception e) {
            System.out.println("parallax " + e);
        }
        add(pane);
    }


    private class Layer extends JPanel{
        int minX, maxX, minY, maxY;
        private JLabel label;
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
