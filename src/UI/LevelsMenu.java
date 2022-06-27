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

import UI.BonusInfoPanel.Bonus;
import UI.miscellaneous.FilledBox;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import UI.PolygonUtilities.Polygon;

import data.DataManager;
import data.LevelIdentifier;
import data.PlayerData;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class LevelsMenu extends JFrame{    

    public JLayeredPane overlayPane;
    JLayeredPane pane;
    static int scrollSpeed = 11;
    int width, height;
    
    Color lightGray = new Color(225,225,225);
    Color darkGray = new Color(148,148,148);

    static Color darkGreen = new Color(23, 63, 31);
    Color darkRed = new Color(190,5,5);

    boolean[] levelsUnlockedEasy = new boolean[5];
    boolean[] levelsUnlockedHard = new boolean[5];
    

    Color lightlightGreen = new Color(98,237,130);
    Color lightGreen = new Color(68,198,98);
    Color lightlightRed = new Color(221,54,73);
    Color lightRed = new Color(201,24,43);

    Color redStroke = new Color(156,0,0);
    Color greenStroke = new Color(27,116,47);
    static String playMode;

    public LevelsMenu levelMenu(){
        return this;
    }

    private void getCurrentPlayMode() {
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
   
    public LevelsMenu() {
        super("Levels");
        getCurrentPlayMode();
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
                        Polygon pol = UI.PolygonUtilities.polygons[UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y))];           
                        pol.fadeIn();
                        //System.out.println(UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y)));
                        pane.moveToFront(pol);
                    } catch (Exception ex) {}
                }
                }
                
        };

        Polygon polygon0 = new Polygon(0,0, lightGreen, lightlightGreen,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70},
                            true,
                            greenStroke,
                            greenStroke,
                            0,
                            "Green"
        );
        Polygon polygon1 = new Polygon(0,0, lightGreen, lightlightGreen,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35},
                            true,
                            greenStroke,
                            greenStroke,
                            1,
                            "Green"
        );
        Polygon polygon2 = new Polygon(0,0, lightRed, lightlightRed,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460},
                            true,
                            redStroke,
                            redStroke,
                            2,
                            "Red"
        );
        Polygon polygon3 = new Polygon(0,0, lightRed, lightlightRed,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895},
                            true,
                            redStroke,
                            redStroke,
                            3,
                            "Red"
        );
        Polygon polygon4 = new Polygon(0,0, lightRed, lightlightRed,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690},
                            true,
                            redStroke,
                            redStroke,
                            4,
                            "Red"
        );


        UI.PolygonUtilities.polygons[0] = polygon0;
        UI.PolygonUtilities.polygons[1] = polygon1;
        UI.PolygonUtilities.polygons[2] = polygon2;
        UI.PolygonUtilities.polygons[3] = polygon3;
        UI.PolygonUtilities.polygons[4] = polygon4;


        pane.add(polygon0, new Integer(0));
        pane.add(polygon1, new Integer(1));
        pane.add(polygon2, new Integer(2));
        pane.add(polygon3, new Integer(3));
        pane.add(polygon4 , new Integer(4));
        

        pane.addMouseMotionListener(adapter);
        pane.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Component component = e.getComponent();
                    Point pt = new Point();
                    pt.y -= e.getYOnScreen() + component.getLocation().y;
                    pt.x -= e.getXOnScreen() + component.getLocation().x; 
                    SwingUtilities.convertPointToScreen(pt, component);
                    int num = UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y));   

                    PlayerData playerData = DataManager.loadPlayerData();  
                    if(playerData.isLevelUnlocked(new LevelIdentifier(playMode, num))){
                        overlayPane = LevelsMenu.starsPane(num);
                        overlayPane.setVisible(true);
                        //add(overlayPane);
                        pane.getParent().add(overlayPane,0);
                        revalidate();
                        repaint();                        
                        for(int j = 0; j < UI.PolygonUtilities.polygons.length; j++){
                            UI.PolygonUtilities.polygons[j].fadeOut();
                        }
                    }
                } catch (Exception ex) {}
            }
        });
        
        try {
            //add(overlayPane);
            //overlayPane.setVisible(false);
            JLabel border = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/mapBorder.png"))));
            border.setBounds(0,0,800,900);
            add(border);
            JLabel sea = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/water.png"))));
            sea.setBounds(410,330,401,569);
            add(sea);
            FilledBox bottomBack = new FilledBox(Color.white);
            bottomBack.setBounds(0,890,800,110);
            add(bottomBack);
        } catch (Exception e) {}
        add(pane);
        setVisible(true);
    }

    //level info panel with star and obstacles descriptions
    public static JLayeredPane starsPane(int num){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        try {
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/stars.png")))));
            ///////
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/filledStar.png")))));
            //////
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles.png")))));

            var obstacles = DataManager.loadLevelData(new LevelIdentifier(playMode, 2)).getObstacleWeights();
            for (String obstacle : obstacles.keySet()) {
                centerPanel.add(UI.InfoPanels.obstacle(obstacle));
            }
            centerPanel.add(UI.InfoPanels.buttonNext());
        } catch (Exception e) {}
        
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(100,100,600,800);
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        pane.add(scroll);
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/panel.png"))));
            image.setBounds(90,90,622,822);
            pane.add(image);
        } catch (Exception e) {System.out.println(e);}
        FilledBox back = new FilledBox(new Color(0,0,0,103));
        back.setBounds(0,0,800,1000);
        pane.add(back);
        pane.setVisible(true);
        return pane;
    }
    
    public void setBonusesPane(){
        pane.remove(overlayPane);
        overlayPane = bonusesPane();
        pane.add(overlayPane,0);
        overlayPane.moveToFront(pane);
        revalidate();
        repaint();
    }
    public static JLayeredPane bonusesPane(){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        BonusInfoPanel bonusInfoPanel = new BonusInfoPanel();
        bonusInfoPanel.passive[0] = bonusInfoPanel.new Bonus("betterBaseLevel", true);
        bonusInfoPanel.passive[1] = bonusInfoPanel.new Bonus("bonusDamage", true);
        bonusInfoPanel.passive[2] = bonusInfoPanel.new Bonus("bonusTime", true);
        bonusInfoPanel.passive[3] = bonusInfoPanel.new Bonus("cooldown", true);
        bonusInfoPanel.passive[4] = bonusInfoPanel.new Bonus("resistance", true);

        bonusInfoPanel.active[0] = bonusInfoPanel.new Bonus("crit", false);
        bonusInfoPanel.active[1] = bonusInfoPanel.new Bonus("dispose", false);
        bonusInfoPanel.active[2] = bonusInfoPanel.new Bonus("merge", false);
        bonusInfoPanel.active[3] = bonusInfoPanel.new Bonus("safeAttack", false);
        bonusInfoPanel.active[4] = bonusInfoPanel.new Bonus("scramble", false);
        bonusInfoPanel.active[5] = bonusInfoPanel.new Bonus("swap", false);
        bonusInfoPanel.active[6] = bonusInfoPanel.new Bonus("upgrade", false);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        try {
            for (Bonus bonus : bonusInfoPanel.passive) {
                centerPanel.add(bonus);
            }
            for (Bonus bonus : bonusInfoPanel.active) {
                centerPanel.add(bonus);
            }
            //centerPanel.add(UI.InfoPanels.buttonNext());
        } catch (Exception e) {}
        
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(100,100,600,800);
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        pane.add(scroll);
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/panel.png"))));
            image.setBounds(90,90,622,822);
            pane.add(image);
        } catch (Exception e) {System.out.println(e);}
        FilledBox back = new FilledBox(new Color(0,0,0,103));
        back.setBounds(0,0,800,1000);
        pane.add(back);
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
