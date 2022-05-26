/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resizableinterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author temak
 */
public class MoveableGradientBox extends GradientBox implements ActionListener{
    Timer timer = new Timer(21, this);
    double moveSize = 3;
    public Point targetPoint;
    JFrame frame;
    
    public MoveableGradientBox(int width, int height, Color firstColor, Color secondColor,directions direction, Point targetPoint, JFrame frame){
        super(width, height, firstColor, secondColor, direction);
        this.targetPoint = targetPoint;
        this.frame = frame;
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
