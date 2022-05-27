package UI;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Moving tile
 * @author Artemii Kolomiichuk
 */
public class MovingTile extends JLabel implements ActionListener{

    public double x, y;
    public int width, height;
    Timer timer = new Timer(16, this);
    double moveSize = 3;
    public Point targetPoint;
    
    public int positionOnField;

    enum state{
        mergeable,
        merged,
        unmergable,
        unmovable
    }
    
    /**
     * Creates a new moving tile.
     * @param image image of the tile.
     * @param targetPoint target point for movement.
     */
    public MovingTile(ImageIcon image, Point targetPoint) {
        setIcon(image); 
        this.targetPoint = targetPoint;
    }
    
    public MovingTile(MovingTile tile){
        x = tile.x;
        y = tile.y;
        width = tile.width;
        height = tile.height;
        moveSize = tile.moveSize;
        targetPoint = tile.targetPoint;
        setIcon(tile.getIcon());
        
    }
    
    /**
     * Starts the timer.
     */
    public void setTimer(){
        timer.start();
    }
    
    /**
     * Sets speed based on the distance between the target point and the current position, considering input speed.
     */
    public void setRelativeSpeed(double speed){
        moveSize = (Math.abs(targetPoint.x - x) + Math.abs(targetPoint.y - y)) * speed / 100.0;
    }
    
    /**
     * Moves the tile to the target point.
     */
    public void actionPerformed(ActionEvent e) {
        //Point start = new Point(getBounds().x, getBounds().y);
        //System.out.println("fff");
        Point end = targetPoint;
        if(Math.abs(x - end.x) <= moveSize && Math.abs(y - end.y) <= moveSize){
            x = end.x;
            y = end.y;
            setBounds((int)x, (int)y, this.getWidth(), this.getHeight()); 
            timer.stop();
        }
        else{
            double deltaX = end.x - x;
            double deltaY = end.y - y;
            if(Math.abs(deltaX) > moveSize)
                x += (deltaX > 0) ? moveSize: -moveSize;
            if(Math.abs(deltaY) > moveSize)
                y += (deltaY > 0) ? moveSize: -moveSize;
            setBounds((int)x, (int)y, this.getWidth(), this.getHeight());  
        } 
    }
}
