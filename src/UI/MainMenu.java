package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
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

import data.DataManager;
import data.LevelIdentifier;
import data.PlayerData;
import models.App;

/**
 * Main menu JFrame with parallax effect
 * @author Artemii Kolomiichuk
 */
public class MainMenu extends JFrame{
    int width, height;
    MouseAdapter moveAdapter;
    
    public MainMenu() {
        super("Neptune-48");
        setResizable(false);
        this.width = 815;
        this.height = 1000;
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
        
        try {
            Layer layer = new Layer(0,0,0,0, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/background.png"))));
            layers.add(layer);
            pane.add(layer,100);

            layer = new Layer(159,203,1,14, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/cloud4.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-24,53,12,36, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/cloud3.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(144,151,165 + 55,169 + 55, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/mount3.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(433,450,41+20 + 75,48+20 + 75, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/mount2.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-188,-148,86 + 55,98 + 55, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/mount1.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-7,-7,655+120,655+120, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/grassTop.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(260,300,505+ 110+100,520+ 110+100, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/bush.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-70,3,477+ 110+120,498+ 110+120, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/bushes.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(595,620,7,16, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/cloud5.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(293,425,12,42, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/cloud2.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-30,180,25 + 20,72 + 20, new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/cloud1.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(90,90,55,55, new ImageIcon(ImageIO.read(App.class.getResourceAsStream("/images/logoGame_invert.png")).getScaledInstance(620,105, 16)));
            layers.add(layer);
            pane.add(layer, 0);


            ImageIcon basicExit = new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/exit.png")));
            ImageIcon highlightExit = new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/exitLight.png")));
            Layer buttonLayerExit = new Layer(185,185,515,515, basicExit);

            layers.add(buttonLayerExit);
            pane.add(buttonLayerExit, 1);

            buttonLayerExit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonLayerExit.setImage(highlightExit);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    buttonLayerExit.setImage(basicExit);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            });
            buttonLayerExit.addMouseMotionListener(moveAdapter);



            ImageIcon basicNew = new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/newGame.png")));
            ImageIcon highlightNew = new ImageIcon(ImageIO.read(App.class.getResource("/images/mainMenu/newGameLight.png")));
            Layer buttonLayerNew = new Layer(185,185,400,400, basicNew);

            layers.add(buttonLayerNew);
            pane.add(buttonLayerNew, 1);

            buttonLayerNew.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonLayerNew.setImage(highlightNew);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    buttonLayerNew.setImage(basicNew);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        DataManager.newPlayerData();
                    } catch (Exception ex) {}
                    models.App.loadLevelsMenuFromMain();
                }
            });
            buttonLayerNew.addMouseMotionListener(moveAdapter);
            

            ContinueButton continueButton = new ContinueButton();
            continueButton.setBounds(185,400-115,414,81);
            pane.add(continueButton, 0);
            continueButton.addMouseMotionListener(moveAdapter);

        } catch (Exception e) {
            System.out.println("main menu error: " + e);
        }
        add(pane);
        pane.addMouseMotionListener(moveAdapter);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private class ContinueButton extends JLabel{
        boolean enabled;
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;

        public ContinueButton(){
            super();  
            try {
                light = new ImageIcon(ImageIO.read(App.class.getResourceAsStream("/images/mainMenu/continueLight.png")));
                normal = new ImageIcon(ImageIO.read(App.class.getResourceAsStream("/images/mainMenu/continue.png")));
                dark = new ImageIcon(ImageIO.read(App.class.getResourceAsStream("/images/mainMenu/continueDark.png")));
                if(DataManager.isPlayerDataAvailable()){
                    enabled = true;
                    setIcon(normal);
                }else{
                    enabled = false;
                    setIcon(dark);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }    
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(enabled){
                        setIcon(light);
                        revalidate();
                        repaint();
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if(enabled){
                        setIcon(normal);
                        revalidate();
                        repaint();
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(enabled){
                        models.App.loadLevelsMenuFromMain();
                    }
                }
            });
            setVisible(true);
        }
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
            this.maxY = minY + (int)((maxY - minY)/2.6);
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
