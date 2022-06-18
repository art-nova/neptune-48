package UI;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Component;
import java.io.File;
import java.util.List;

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
import javax.swing.Timer;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;

import UI.fragments.FilledBox;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author Artemii Kolomiichuk
 */
public class LevelsMenu extends JFrame{    

    JLayeredPane overlayPane;


    int width, height;
    Polygon[] polygons = new Polygon[5];
    Color lightGray = new Color(225,225,225);
    Color darkGray = new Color(148,148,148);

    static Color darkGreen = new Color(23, 63, 31);
    Color lightRed = new Color(225,91,94);
    Color lightLightRed = new Color(255,128,128);
    Color darkRed = new Color(190,5,5);

    public LevelsMenu() {
        super("Levels");

        this.width = 800;
        this.height = 1000;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);

        JLayeredPane pane = new JLayeredPane();
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
                        for(int j = 0; j < polygons.length; j++){
                            polygons[j].moveToOrigin();
                            pane.moveToFront(polygons[j]);
                        }
                        Polygon pol = polygons[getMostFrequent(getPolygons(pt.x, pt.y))];           
                        pol.moveUp();
                        pane.moveToFront(pol);
                    } catch (Exception ex) {}
                }
                }
                
        };

        Polygon polygon0 = new Polygon(0,0, lightGray, lightLightRed,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70}
        );
        Polygon polygon1 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35}
        );
        Polygon polygon2 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460}
        );
        Polygon polygon3 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895}
        );
        Polygon polygon4 = new Polygon(0,0, lightRed, lightLightRed,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690}
        );


        Polygon polygonPad0 = new Polygon(0,0, darkGray, darkGray,
                            new int[]{570,800,800,620,635,600,580,570,595,590,560,555,580},
                            new int[]{0,0,    350,370,325,290,290,260,210,140,130,85,70}
        );
        Polygon polygonPad1 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,570,580,555,560, 590, 595,570,580,600,635,620,490,410,382,350,385,345,300,290,260,240,270,245,125,140,75,30},
                            new int[]{0, 0, 70,  85,130,  140,210,260,290,290,325,370,425,420,455,445,415,310,300,200,190,170,155,115, 85,50, 20,35}
        );
        Polygon polygonPad2 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,0,  30,75,140,125,245,270,240,260,290,300,345,385,350,382,410,490,470,370,380,365,345,350,305,310,270,220,170,150,110,115,73,70},
                            new int[]{450,0,35,20,50,  85,115,155,170,190,200,300,310,415,445,455,420,425,520,530,555,570,560,530,525,495,490,525,515,535,535,500,500,460}
        );
        Polygon polygonPad3 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{0,0,     70, 73,115, 110,150,170,220,270, 310,305,350,345,365,380, 370,470,440,373,370,330,320,330,310,340,380,395,425,435},
                            new int[]{900,450,460,500,500, 535,535,515,525,490, 495,525,530,560,570,555, 530,520,680,690,710,720,760,800,820,850,825,840,840,895}
        );
        Polygon polygonPad4 = new Polygon(0,0, darkRed, darkRed,
                            new int[]{440,425,395,380,340,310,330,320,330,370,373},
                            new int[]{680,840,840,825,850,820,800,760,720,710,690}
        );
        
        polygons[0] = polygon0;
        polygons[1] = polygon1;
        polygons[2] = polygon2;
        polygons[3] = polygon3;
        polygons[4] = polygon4;

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
                    int num = getMostFrequent(getPolygons(pt.x, pt.y));           
                    if(num == 0){
                        System.out.println("Level 0");
                        overlayPane = LevelsMenu.starsPane();
                        overlayPane.setVisible(true);
                        pane.add(overlayPane, 0);
                        
                        for(int j = 0; j < polygons.length; j++){
                            polygons[j].moveToOrigin();
                        }
                        overlayPane.moveToFront(pane);
                    }
                } catch (Exception ex) {}
            }
        });
            
        add(pane);
        setVisible(true);
    }


    public static JLayeredPane starsPane(){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        
        
        

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        try {
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/headers/stars.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/time.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/filledStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/emptyStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/emptyStar.png")))));
            centerPanel.add(new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/headers/obstacles.png")))));
            centerPanel.add(UI.ObstaclesPanels.obstacle("dispose"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("downgrade"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("freeze"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("garbage"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("scramble"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("subtractTime"));
            centerPanel.add(UI.ObstaclesPanels.obstacle("swap"));
        } catch (Exception e) {}
        
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(100,100,600,800);
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());


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
    
    //Polygon class
    private class Polygon extends JComponent implements ActionListener{
        int x,y;
        public int[] xs;
        public int[] ys;
        private Timer timer;
        private double moveSize = 1;
        private Point targetPoint;
        int originX, originY;
        Color colorBase, colorHighlight, colorFrom, colorTo;

        public Polygon(int x,int y, Color colorBase, Color colorHighlight, int[] xs, int[] ys){
            super();
            timer = new Timer(17, this);
            originX = x;
            originY = y;
            this.x = x;
            this.y = y;
            this.xs = xs;
            this.ys = ys;
            this.colorBase = colorBase;
            this.colorHighlight = colorHighlight;
            setBounds(x,y,2000,2000);
            setVisible(true);
            setBackground(new Color(0,0,0,0));
            setForeground(colorBase);
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.fillPolygon(xs, ys, xs.length); 
        }

        public Line[] toLines(){
            Line[] lines = new Line[xs.length];
            for(int i = 0; i < xs.length - 1; i++){
                lines[i] = new Line(new Point(xs[i], ys[i]), new Point(xs[i+1], ys[i+1]));
            }
            lines[xs.length - 1] = new Line(new Point(xs[xs.length - 1], ys[xs.length - 1]), new Point(xs[0], ys[0]));
            return lines;
        }

        public void moveToOrigin(){
            targetPoint = new Point(originX, originY);
            colorFrom = colorHighlight;
            colorTo = colorBase;
            timer.start();
        }

        public void moveUp(){
            targetPoint = new Point(originX + 10, originY - 10);
            colorFrom = colorBase;
            colorTo = colorHighlight;
            timer.start();
        }

        public void actionPerformed(ActionEvent e) {
            Point end = targetPoint;
            if(Math.abs(x - end.x) <= moveSize && Math.abs(y - end.y) <= moveSize){
                x = end.x;
                y = end.y;
                setLocation((int)x, (int)y); 
                timer.stop();
            }
            else{
                double deltaX = end.x - x;
                double deltaY = end.y - y;
                if(Math.abs(deltaX) > moveSize)
                    x += (deltaX > 0) ? moveSize: -moveSize;
                if(Math.abs(deltaY) > moveSize)
                    y += (deltaY > 0) ? moveSize: -moveSize;
                setLocation((int)x, (int)y); 
            
            int percentage = 0;
            try
            {
                System.out.println("x - originX: " + (x - originX) + " targetPoint.x - originX: " + (targetPoint.x - originX));
                percentage = (int)(Math.abs((x - originX)/(double)(targetPoint.x - originX)) * 100);
            }
            catch (Exception e2){}
            if(x-originX == 0 && targetPoint.x - originX == 0){
            }
            else{
                if(percentage <= 100 && percentage >= 0) {
                        System.out.println(percentage);
                        setForeground(UI.miscellaneous.Utilities.colorBetween(colorFrom, colorTo, percentage));
                    }
            }
        }
            revalidate();
            repaint();
        }
    }

    //two dots that form a line
    private class Line{
        public Point first;
        public Point second;
        public Line(Point first, Point second){
            this.first = first;
            this.second = second;
        }
        @Override
        public String toString() {
            return "Line{" + "first=" + first + ",\n second=" + second + '}';
        }
    }

    /* Polygon-Lines utilities */
    //returns dot where two lines segments intersect
    private Point getIntersection(Line l1, Line l2){
        double x1 = l1.first.x;
        double y1 = l1.first.y;
        double x2 = l1.second.x;
        double y2 = l1.second.y;
        double x3 = l2.first.x;
        double y3 = l2.first.y;
        double x4 = l2.second.x;
        double y4 = l2.second.y;
        double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if(denom == 0)
            return null;
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denom;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denom;
        return new Point((int)xi, (int)yi);
    }
    
    //checks if dot is on line
    private boolean isOnLine(Line l, Point p){
        double x1 = l.first.x;
        double y1 = l.first.y;
        double x2 = l.second.x;
        double y2 = l.second.y;
        double x = p.x;
        double y = p.y;
        return (x - x1) * (x - x2) <= 0 && (y - y1) * (y - y2) <= 0;
    }
    
    //checks if lines intersect
    private boolean linesIntersect(Line line1, Line line2){
        Point p = getIntersection(line1, line2);
        if(p == null)
            return false;
        return isOnLine(line1, p) && isOnLine(line2, p);
    }

    //return how many times line and polygons lines intersect
    private int getIntersections(int x, int y, Polygon polygon){
        Line line = new Line(new Point(-x, -y), new Point( 5000, -y));
        int count = 0;
        for(Line l : polygon.toLines()){
            if(linesIntersect(line, l))
                count++;
        }
        return count;
    }
    
    //returns polygon by given coordinates
    private int getPolygon(int x, int y){
        for(int i = 0; i < polygons.length; i++){
            if(getIntersections(x, y, polygons[i])%2 == 1){
               return i;
            }
        }
        return -1;
    }

    //checks in which polygon points around the point are
    private int[] getPolygons(int x, int y){
        int[] polygon = new int[9];
        int counter = 0;
        for(int i =-1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                int index = getPolygon(x + i, y + j);
                polygon[counter] = index;
                counter++;
            }
        }
        return polygon;
    }

    //sorts array and return the most frequently appearing element
    private int getMostFrequent(int[] array){
        int[] count = new int[array.length];
        for(int i = 0; i < array.length; i++){
            count[i] = 0;
            for(int j = 0; j < array.length; j++){
                if(array[i] == array[j])
                    count[i]++;
            }
        }
        int max = 0;
        int index = 0;
        for(int i = 0; i < count.length; i++){
            if(count[i] > max){
                max = count[i];
                index = i;
            }
        }
        return array[index];
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
