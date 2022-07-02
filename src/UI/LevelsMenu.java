package UI;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Component;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import UI.AbilityInfoPanel.AbilityBar;
import UI.LevelMenu.Ability;
import UI.miscellaneous.FilledBox;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import UI.PolygonUtilities.Polygon;

import data.DataManager;
import data.LevelIdentifier;
import data.PlayerData;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class LevelsMenu extends JFrame{    

    public static JLayeredPane overlayPane;
    static JLayeredPane pane;
    static int scrollSpeed = 11;
    int width, height;

    static Color darkGreen = new Color(23, 63, 31);

    Color lightlightGreen = new Color(98,237,130);
    Color lightGreen = new Color(68,198,98);
    Color lightlightRed = new Color(221,54,73);
    Color lightRed = new Color(201,24,43);
    Color darkRed = new Color(132,16,28);

    Color redStroke = new Color(156,0,0);
    Color greenStroke = new Color(27,116,47);
    static String playMode;
    static public TopPanel topPanel;
    public LevelsMenu levelMenu(){
        return this;
    }

    private void getCurrentPlayMode(String hardness) {
        if(hardness.equals("hard") || hardness.equals("normal")){
            playMode = hardness;
        }
        else{
            try {
                PlayerData playerData = DataManager.loadPlayerData();  
                if(playerData.isLevelUnlocked(new LevelIdentifier("hard", 0))){
                    playMode = "hard";
                }
                else{
                    playMode = "normal";
                }
            } catch (Exception e) {
                playMode = "normal";
            }
        }
    }
   
    public LevelsMenu(String hardness) {
        super("Levels");
        getCurrentPlayMode(hardness);
        this.width = 815;
        this.height = 1000;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);

        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(width, height));
        pane.setBounds(0,0,width,height);
        overlayPane = null;
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(overlayPane == null){
                    Component component = e.getComponent();
                    Point pt = new Point();
                    pt.y -= e.getYOnScreen() + component.getLocation().y;
                    pt.x -= e.getXOnScreen() + component.getLocation().x; 
                    SwingUtilities.convertPointToScreen(pt, component);
                        try {
                            for(int j = 0; j < UI.PolygonUtilities.polygons.length; j++){
                                UI.PolygonUtilities.polygons[j].fadeOut();
                                //pane.moveToFront(UI.PolygonUtilities.polygons[j]);
                            }
                            if(pt.y < -145){
                            Polygon pol = UI.PolygonUtilities.polygons[UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y))];           
                            pol.fadeIn();
                            //System.out.println(UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y)));
                            pane.moveToFront(pol);
                            }
                        } catch (Exception ex) {}
                    
                    }
                }
                
        };

        Color polygonColor ;
        Color polygonColorLighter;
        String colorOverlay;
        PlayerData playerData = null;
        Color stroke;
        try {
            playerData = DataManager.loadPlayerData(); 
        } catch (Exception e) {}


        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 0))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 0))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        Polygon polygon0 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70},
                            true,
                            stroke,
                            stroke,
                            0,
                            colorOverlay
        );

        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 1))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 1))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        Polygon polygon1 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35},
                            true,
                            stroke,
                            stroke,
                            1,
                            colorOverlay
        );

        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 2))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 2))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        Polygon polygon2 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460},
                            true,
                            stroke,
                            stroke,
                            2,
                            colorOverlay
        );

        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 3))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 3))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        Polygon polygon3 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895},
                            true,
                            stroke,
                            stroke,
                            3,
                            colorOverlay
        );

        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 4))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 4))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }

        Polygon polygon4 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690},
                            true,
                            stroke,
                            stroke,
                            4,
                            colorOverlay
        );

        if(playerData.isLevelCompleted(new LevelIdentifier(playMode, 5))){
            polygonColor = lightGreen;
            polygonColorLighter = lightlightGreen;
            colorOverlay = "Green";
            stroke = greenStroke;
        }
        else if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, 5))){
            polygonColor = lightRed;
            polygonColorLighter = lightlightRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }
        else{
            polygonColor = darkRed;
            polygonColorLighter = darkRed;
            colorOverlay = "Red";
            stroke = redStroke;
        }

        Polygon polygon5 = new Polygon(0,0, polygonColor, polygonColorLighter,
                            new int[]{486,537,744,759,750,660,660,621,613,600,595,555,542},
                            new int[]{801,849,849,820,807,807,781,769,735,735,769,780,814},
                            true,
                            stroke,
                            stroke,
                            4,
                            "null"
        );


        UI.PolygonUtilities.polygons[0] = polygon0;
        UI.PolygonUtilities.polygons[1] = polygon1;
        UI.PolygonUtilities.polygons[2] = polygon2;
        UI.PolygonUtilities.polygons[3] = polygon3;
        UI.PolygonUtilities.polygons[4] = polygon4;
        UI.PolygonUtilities.polygons[5] = polygon5;


        pane.add(polygon0, Integer.valueOf(0));
        pane.add(polygon1, Integer.valueOf(1));
        pane.add(polygon2, Integer.valueOf(2));
        pane.add(polygon3, Integer.valueOf(3));
        pane.add(polygon4 , Integer.valueOf(4));
        
        

        pane.addMouseMotionListener(adapter);
        pane.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if(overlayPane == null){
                        Component component = e.getComponent();
                    Point pt = new Point();
                    pt.y -= e.getYOnScreen() + component.getLocation().y;
                    pt.x -= e.getXOnScreen() + component.getLocation().x; 
                    SwingUtilities.convertPointToScreen(pt, component);
                    int num = UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y));   
                    
                    if(pt.y < -145){
                        PlayerData playerData = DataManager.loadPlayerData();  
                        if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, num))){
                            overlayPane = LevelsMenu.starsPane(num, new LevelIdentifier(playMode, num));
                            overlayPane.setVisible(true);
                            //add(overlayPane);
                            pane.getParent().add(overlayPane,0);
                            revalidate();
                            repaint();                        
                            for(int j = 0; j < UI.PolygonUtilities.polygons.length; j++){
                                UI.PolygonUtilities.polygons[j].fadeOut();
                            }
                        }
                    }
                    }                    
                } catch (Exception ex) {}
            }
        });
        
        try {
            //add(overlayPane);
            //overlayPane.setVisible(false);
            JLabel border = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/mapBorder.png"))));
            border.setBounds(0,138,800,824);
            add(border);
            add(polygon5);
            JLabel sea = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/water.png"))));
            sea.setBounds(410,330+80,401,569);
            add(sea);
            topPanel = new TopPanel();
            add(topPanel);
        } catch (Exception e) {}
        
        init();
        add(pane);
        setVisible(true);
        
    }

    public static class TopPanel extends JLayeredPane{

        public JLabel abilityFirst;
        public JLabel abilitySecond;
        public JLabel abilityThird;
        public TopPanel(){
            super();
            setBounds(0,0,800,145);
            try {
                ImageIcon back = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/back.png")));
                ImageIcon backLight = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/backLight.png")));
                JLabel backButton = new JLabel(back);
                backButton.setBounds(33,13,122,112);
                backButton.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent e){
                        backButton.setIcon(backLight);
                    }
                    @Override
                    public void mouseExited(MouseEvent e){
                        backButton.setIcon(back);
                    }
                    @Override
                    public void mouseClicked(MouseEvent e){
                        App.loadMainMenuFromLevels();
                    }
                });
                add(backButton);

                PlayerData playerData = DataManager.loadPlayerData();  
                boolean hardModeUnlocked = playerData.isLevelUnlocked(new LevelIdentifier("hard", 0));


                ImageIcon normal = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/normal.png")));
                ImageIcon normalLight = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/normalLight.png")));

                ImageIcon hard = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/hard.png")));
                ImageIcon hardLight = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/hardLight.png")));
                ImageIcon hardDark = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/hardDark.png")));

                JLabel normalButton = new JLabel(normal);
                JLabel hardButton = new JLabel(hard);
                normalButton.setBounds(583,14,202,53);
                hardButton.setBounds(583,78,202,53);

                if(playMode.equals("normal")){
                    normalButton.setIcon(normalLight);
                }
                if(hardModeUnlocked){
                    if(playMode.equals("hard")){
                        hardButton.setIcon(hardLight);
                    }
                }
                else{
                    hardButton.setIcon(hardDark);
                }
                normalButton.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent e){
                        if(playMode.equals("hard")){
                            normalButton.setIcon(normalLight);
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e){
                        if(playMode.equals("hard")){
                            normalButton.setIcon(normal);
                        }
                    }
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(playMode.equals("hard")){
                            App.loadLevelsMenuFromLevels("normal");
                        }
                    }
                });

                hardButton.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent e){
                        if(hardModeUnlocked){
                            if(playMode.equals("normal")){
                                hardButton.setIcon(hardLight);
                            }
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e){
                        if(hardModeUnlocked){
                            if(playMode.equals("normal")){
                                hardButton.setIcon(hard);
                            }
                        }
                    }
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(hardModeUnlocked){
                            if(playMode.equals("normal")){
                                App.loadLevelsMenuFromLevels("hard");
                            }
                        }
                    }
                });
                
                JLabel abilitiesPlacement = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilitiesPlacement.png"))));
                abilitiesPlacement.setBounds(189,21,370,104);
                abilityFirst = new JLabel();
                abilityFirst.setBounds(188,21,106,106);
                add(abilityFirst);
                abilityFirst.setVisible(false);
                abilitySecond = new JLabel();
                abilitySecond.setBounds(320,21,106,106);
                add(abilitySecond);
                abilitySecond.setVisible(false);
                abilityThird = new JLabel();
                abilityThird.setBounds(450,18,110,110);
                add(abilityThird);
                abilityThird.setVisible(false);
                add(abilitiesPlacement);
                add(normalButton);
                add(hardButton);

                FilledBox abilitiesClickTrecker = new FilledBox(new Color(23,63,31));
                abilitiesClickTrecker.setBounds(170,0,400,145);
                abilitiesClickTrecker.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(overlayPane == null){
                            System.out.println("abilities");
                            overlayPane = AbilityInfoPanel.getAbilitiesPanel();
                            overlayPane.setVisible(true);
                            //add(overlayPane);
                            getParent().add(overlayPane,0);
                            revalidate();
                            repaint();  
                        }
                        else{
                            overlayPane.setVisible(false);
                            getParent().remove(overlayPane);
                            overlayPane = null;
                            revalidate();
                            repaint();
                        }
                    }
                });
                add(abilitiesClickTrecker);
            } catch (Exception ex) {}
            setBackground(new Color(23,63,31));
            setOpaque(true);
            setVisible(true);
        }


        public void setAbility(int ability, ImageIcon icon){
            if(ability == 1){
                abilityFirst.setVisible(true);
                abilityFirst.setIcon(new ImageIcon(icon.getImage().getScaledInstance(106,106, Image.SCALE_SMOOTH)));
            }
            else if(ability == 2){
                if(!abilityFirst.isVisible()){
                    setAbility(1, icon);
                }
                else{
                    abilitySecond.setVisible(true);
                    abilitySecond.setIcon(new ImageIcon(icon.getImage().getScaledInstance(106,106, Image.SCALE_SMOOTH)));
                }
            }
            else if(ability == 3){
                abilityThird.setVisible(true);
                abilityThird.setIcon(new ImageIcon(icon.getImage().getScaledInstance(110,110, Image.SCALE_SMOOTH)));
            }
        }
        public void removeAbility(int ability){
            if(ability == 1){
                abilityFirst.setVisible(false);
                if(abilitySecond.isVisible()){
                    abilityFirst.setIcon(abilitySecond.getIcon());
                    abilitySecond.setVisible(false);
                    abilityFirst.setVisible(true);
                }
            }
            else if(ability == 2){
                abilitySecond.setVisible(false);
            }
            else if(ability == 3){
                abilityThird.setVisible(false);
            }
        }
    }

    private void init(){
        try {
            overlayPane = AbilityInfoPanel.getAbilitiesPanel();
            overlayPane = null;
            try {
                if(DataManager.loadPlayerData().getActiveAbility2() != null){
                    topPanel.setAbility(2, new ImageIcon(ImageIO.read(new File("resources/images/level/" + DataManager.loadPlayerData().getActiveAbility2() + ".png"))));
                }

            } catch (Exception e) {}
        } catch (Exception e) {
            System.out.println("error:" + e);
        }
    }

    //TODO
    //level info panel with star and obstacles descriptions
    public static JLayeredPane starsPane(int num, LevelIdentifier levelIdentifier){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(23,63,31));
        try {
            centerPanel.add(InfoPanels.starsPanel(levelIdentifier));
            //////
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles.png")))));

            var obstacles = DataManager.loadLevelData(new LevelIdentifier(playMode, num)).getObstacleWeights();
            for (String obstacle : obstacles.keySet()) {
                centerPanel.add(UI.InfoPanels.obstacle(obstacle));
            }
            centerPanel.add(UI.InfoPanels.buttonNext(new LevelIdentifier(playMode, num)));
        } catch (Exception e) {}
        
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(100,80,600,800);
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        pane.add(scroll);
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/panel.png"))));
            image.setBounds(90,70,622,822);
            pane.add(image);
        } catch (Exception e) {System.out.println(e);}
        FilledBox back = new FilledBox(new Color(0,0,0,103));
        back.setBounds(0,0,800,1000);
        pane.add(back);
        back.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                pane.getParent().remove(overlayPane);
                overlayPane = null;
                App.getLevelsMenu().revalidate();
                App.getLevelsMenu().repaint();
            }
        });
        pane.setVisible(true);
        return pane;
    }

    static class CustomScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(darkGreen);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            super.uninstallComponents();
            c.setBackground(darkGreen);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        }
    }
}
