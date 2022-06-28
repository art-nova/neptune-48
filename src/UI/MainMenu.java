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

/**
 * Main menu JFrame with parallax effect
 * @author Artemii Kolomiichuk
 */
public class MainMenu extends JFrame{
    int width, height;
    MouseAdapter moveAdapter;
    
    public MainMenu() {
        super("Menu");
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

            layer = new Layer(144,151,165,169, new ImageIcon(ImageIO.read(new File(p + "mount3.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(433,450,41+20,48+20, new ImageIcon(ImageIO.read(new File(p + "mount2.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-188,-148,86,98, new ImageIcon(ImageIO.read(new File(p + "mount1.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-7,-7,655+20,655+20, new ImageIcon(ImageIO.read(new File(p + "grassTop.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(250,310,505+ 110,520+ 110, new ImageIcon(ImageIO.read(new File(p + "bush.png"))));
            layers.add(layer);                
            pane.add(layer, 0);

            layer = new Layer(-102,3,477+ 110,498+ 110, new ImageIcon(ImageIO.read(new File(p + "bushes.png"))));
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

            layer = new Layer(90,90,55,55, new ImageIcon(ImageIO.read(new File("resources/images/logoGame_invert.png")).getScaledInstance(620,105, 16)));
            layers.add(layer);
            pane.add(layer, 0);


            ImageIcon basicExit = new ImageIcon(ImageIO.read(new File(p + "exit.png")));
            ImageIcon highlightExit = new ImageIcon(ImageIO.read(new File(p + "exitLight.png")));
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



            ImageIcon basicNew = new ImageIcon(ImageIO.read(new File(p + "newGame.png")));
            ImageIcon highlightNew = new ImageIcon(ImageIO.read(new File(p + "newGameLight.png")));
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
    }

    private class ContinueButton extends JLabel{
        boolean enabled;
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;

        public ContinueButton(){
            super();  
            try {
                light = new ImageIcon(ImageIO.read(new File("resources/images/mainMenu/continueLight.png")));
                normal = new ImageIcon(ImageIO.read(new File("resources/images/mainMenu/continue.png")));
                dark = new ImageIcon(ImageIO.read(new File("resources/images/mainMenu/continueDark.png")));
                PlayerData playerData = DataManager.loadPlayerData();  
                if(playerData.isLevelCompleted(new LevelIdentifier("normal", 0))){
                    enabled = true;
                    setIcon(normal);
                }else{
                    enabled = false;
                    setIcon(dark);
                }
            } catch (Exception e) {
                try {
                    setIcon(dark);
                    enabled = false;
                } catch (Exception ex) {}
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
            this.maxY = minY + (int)((maxY - minY)/3.2);
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
