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
/**
 *
 * @author Artemii Kolomiichuk
 */
public class LevelsMenu extends JFrame{    

    public JLayeredPane overlayPane;
    JLayeredPane pane;
    static int scrollSpeed = 8;
    int width, height;
    
    Color lightGray = new Color(225,225,225);
    Color darkGray = new Color(148,148,148);

    static Color darkGreen = new Color(23, 63, 31);
    Color lightRed = new Color(225,91,94);
    Color lightLightRed = new Color(255,128,128);
    Color darkRed = new Color(190,5,5);

    boolean[] levelsUnlockedEasy = new boolean[5];
    boolean[] levelsUnlockedHard = new boolean[5];

    public LevelsMenu levelMenu(){
        return this;
    }

    public LevelsMenu() {
        super("Levels");

        this.width = 827;
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
                            pane.moveToFront(UI.PolygonUtilities.polygons[j]);
                        }
                        Polygon pol = UI.PolygonUtilities.polygons[UI.PolygonUtilities.getMostFrequent(UI.PolygonUtilities.getPolygons(pt.x, pt.y))];           
                        pol.fadeIn();
                        pane.moveToFront(pol);
                    } catch (Exception ex) {}
                }
                }
                
        };

        Color strokeBase = new Color(200,0,0);
        Color strokeHighlight = new Color(250,50,50);
        Polygon polygon0 = new Polygon(0,0, lightGray, lightLightRed,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70},
                            true,
                            strokeBase,
                            strokeHighlight
        );
        Polygon polygon1 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35},
                            true,
                            strokeBase,
                            strokeHighlight
        );
        Polygon polygon2 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460},
                            true,
                            strokeBase,
                            strokeHighlight
        );
        Polygon polygon3 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895},
                            true,
                            strokeBase,
                            strokeHighlight
        );
        Polygon polygon4 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690},
                            true,
                            strokeBase,
                            strokeHighlight
        );


        Polygon polygonPad0 = new Polygon(0,0, darkGray, darkGray,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70},
                            true,
                            darkGray,
                            strokeHighlight
        );
        Polygon polygonPad1 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35},
                            true,
                            darkRed,
                            strokeHighlight
        );
        Polygon polygonPad2 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460},
                            true,
                            darkRed,
                            strokeHighlight
        );
        Polygon polygonPad3 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895},
                            true,
                            darkRed,
                            strokeHighlight
        );
        Polygon polygonPad4 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690},
                            true,
                            darkRed,
                            strokeHighlight
        );
        
        UI.PolygonUtilities.polygons[0] = polygon0;
        UI.PolygonUtilities.polygons[1] = polygon1;
        UI.PolygonUtilities.polygons[2] = polygon2;
        UI.PolygonUtilities.polygons[3] = polygon3;
        UI.PolygonUtilities.polygons[4] = polygon4;

        pane.add(polygonPad0, 100);
        pane.add(polygonPad1, 100);
        pane.add(polygonPad2, 100);
        pane.add(polygonPad3, 100);
        pane.add(polygonPad4, 100);  

        pane.add(polygon0, 0);
        pane.add(polygon1, 0);
        pane.add(polygon2, 0);
        pane.add(polygon3, 0);
        pane.add(polygon4 , 0);
        

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
                    if(num == 0){
                        System.out.println("Level 0");
                        overlayPane = LevelsMenu.starsPane();
                        overlayPane.setVisible(true);
                        pane.add(overlayPane, 0);
                        
                        for(int j = 0; j < UI.PolygonUtilities.polygons.length; j++){
                            UI.PolygonUtilities.polygons[j].fadeOut();
                        }
                        overlayPane.moveToFront(pane);
                    }
                } catch (Exception ex) {}
            }
        });
            
        add(pane);
        setVisible(true);
    }

    //level info panel with star and obstacles descriptions
    public static JLayeredPane starsPane(){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        
        
        

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        try {
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/stars.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/time.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/filledStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/emptyStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/emptyStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles.png")))));
            centerPanel.add(UI.InfoPanels.obstacle("dispose"));
            centerPanel.add(UI.InfoPanels.obstacle("downgrade"));
            centerPanel.add(UI.InfoPanels.obstacle("freeze"));
            centerPanel.add(UI.InfoPanels.obstacle("garbage"));
            centerPanel.add(UI.InfoPanels.obstacle("scramble"));
            centerPanel.add(UI.InfoPanels.obstacle("subtractTime"));
            centerPanel.add(UI.InfoPanels.obstacle("swap"));
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
