package UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.imageio.ImageIO;

public class PolygonUtilities {
    static Polygon[] polygons = new Polygon[5];

    public static class Polygon extends JLayeredPane implements ActionListener{
        int x,y;
        public int[] xs;
        public int[] ys;
        private Timer timer;
        Color colorBase, colorHighlight, colorFrom, colorTo, currentColor;
        double colorDistance;
        boolean hasStroke;
        JLabel overlay;
        public Polygon(int x,int y, Color colorBase, Color colorHighlight, int[] xs, int[] ys, boolean hasStroke, Color baseStroke, Color highlightStroke, int num, String overlayColor){
            super();
            timer = new Timer(27 + (int)Math.round(Math.random() * 5), this);
            this.x = x;
            this.y = y;
            this.xs = xs;
            this.hasStroke = hasStroke;
            this.ys = ys;
            int yOffset = 45;
            for (int i = 0; i < ys.length; i++) {
                ys[i] += yOffset;
            }
            yOffset = 92;
            this.colorBase = colorBase;
            this.colorHighlight = colorHighlight;
            if(hasStroke){
                add(new Polygon(x, y, baseStroke, highlightStroke, xs, ys, false, null, null, num, ""));
            }
            try {
                if(overlayColor.equalsIgnoreCase("green") || overlayColor.equalsIgnoreCase("red")){
                    overlay = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/"+ num + "overlay" + overlayColor + ".png"))));
                    switch(num){
                        case 0:
                            overlay.setBounds(572,2 + yOffset,240,370);
                            break;
                        case 1:
                            overlay.setBounds(121, -2 + yOffset, 505, 420);
                            break;
                        case 2:
                            overlay.setBounds(0,34 + yOffset,480,495);
                            break;
                        case 3:
                            overlay.setBounds(0,461 + yOffset,435,445);
                            break;
                        case 4:
                            overlay.setBounds(337,728 + yOffset,70,85);
                            break;
                    }
                    add(overlay);
                }
            } catch (Exception e) {}
            
            setBounds(x,y,2000,2000);
            setVisible(true);
            setBackground(new Color(0,0,0,0));
            setForeground(colorBase);
            colorFrom = colorBase;
            colorTo = colorBase;
            currentColor = colorBase;
            colorDistance = UI.miscellaneous.Utilities.colorDistance(colorBase, colorHighlight);
            timer.start();
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(hasStroke){
                Graphics2D g2 = (Graphics2D) g;
                g2.drawPolygon(xs, ys, xs.length);
                g2.fillPolygon(xs, ys, xs.length); 
            }
            else{
                Graphics2D g2 = (Graphics2D) g;            
                g2.setStroke(new BasicStroke(6));
                g2.drawPolygon(xs, ys, xs.length);
            } 
        }

        public Line[] toLines(){
            Line[] lines = new Line[xs.length];
            for(int i = 0; i < xs.length - 1; i++){
                lines[i] = new Line(new Point(xs[i], ys[i]), new Point(xs[i+1], ys[i+1]));
            }
            lines[xs.length - 1] = new Line(new Point(xs[xs.length - 1], ys[xs.length - 1]), new Point(xs[0], ys[0]));
            return lines;
        }

        public void fadeOut(){
            colorFrom = currentColor;
            colorTo = colorBase;
        }

        public void fadeIn(){
            colorFrom = currentColor;
            colorTo = colorHighlight;
        }

        public void actionPerformed(ActionEvent e) {
            int percentage = (int)Math.round((colorDistance - UI.miscellaneous.Utilities.colorDistance(colorTo, currentColor)) / colorDistance * 100);
            //System.out.println(percentage);
            if(percentage > 100){
                percentage = 100;
            }
            if(percentage <= 100 && percentage >= 0) {
                currentColor = UI.miscellaneous.Utilities.colorBetween(colorFrom, colorTo, percentage+2);
                setForeground(currentColor);
                //System.out.println(UI.miscellaneous.Utilities.colorDistance(currentColor, colorTo));
            }
        }
    }

    //two dots that form a line
    public static class Line{
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
    public static Point getIntersection(Line l1, Line l2){
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
    public static boolean isOnLine(Line l, Point p){
        double x1 = l.first.x;
        double y1 = l.first.y;
        double x2 = l.second.x;
        double y2 = l.second.y;
        double x = p.x;
        double y = p.y;
        return (x - x1) * (x - x2) <= 0 && (y - y1) * (y - y2) <= 0;
    }
    
    //checks if lines intersect
    public static boolean linesIntersect(Line line1, Line line2){
        Point p = getIntersection(line1, line2);
        if(p == null)
            return false;
        return isOnLine(line1, p) && isOnLine(line2, p);
    }

    //return how many times line and polygons lines intersect
    public static int getIntersections(int x, int y, Polygon polygon){
        Line line = new Line(new Point(-x, -y), new Point( 5000, -y));
        int count = 0;
        for(Line l : polygon.toLines()){
            if(linesIntersect(line, l))
                count++;
        }
        return count;
    }
    
    //returns polygon by given coordinates
    public static int getPolygon(int x, int y){
        for(int i = 0; i < polygons.length; i++){
            if(getIntersections(x, y, polygons[i])%2 == 1){
               return i;
            }
        }
        return -1;
    }

    //checks in which polygon points around the point are
    public static int[] getPolygons(int x, int y){
        int[] polygon = new int[5];
        int counter = 0;
        for(int i =-1; i < 1; i++){
            int index = getPolygon(x + i, y);
            polygon[counter] = index;
            counter++;
            index = getPolygon(x, y + i);
            polygon[counter] = index;
            counter++;
        }
        return polygon;
    }

    //sorts array and return the most frequently appearing element
    public static int getMostFrequent(int[] array){
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
}
