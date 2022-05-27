package UI.fragments;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Movable gradient colored box.
 * @author Artemii Kolomiichuk
 */
public class MoveableGradientBox extends GradientBox implements ActionListener{
    Timer timer = new Timer(21, this);
    double moveSize = 3;
    public Point targetPoint;
    
    /**
     * Creates a new  movable gradient colored box.
     * @param width width of the box.
     * @param height height of the box.
     * @param firstColor first color of the gradient.
     * @param secondColor second color of the gradient.
     * @param direction direction of the gradient.
     * @param targetPoint target point for movement.
     */
    public MoveableGradientBox(int width, int height, Color firstColor, Color secondColor,directions direction, Point targetPoint){
        super(width, height, firstColor, secondColor, direction);
        this.targetPoint = targetPoint;
    }
    
    public void setTimer(){
        timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
        Point start = new Point(getBounds().x, getBounds().y);
        Point end = targetPoint;
        
        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
        
        if(Math.abs(deltaX) > moveSize)
            x += (deltaX > 0) ? moveSize: -moveSize;
        if(Math.abs(deltaY) > moveSize)
            y += (deltaY > 0) ? moveSize: -moveSize;
        setBounds((int)x, (int)y, width, height);       
    }
}
