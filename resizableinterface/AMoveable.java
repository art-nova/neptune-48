/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resizableinterface;

import java.awt.Component;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author temak
 */
abstract class AMoveable extends Component implements ActionListener{
    
    public int x;
    public int y;
    public int width;
    public int height;
    double moveSize = 3;
    public Point targetPoint = null;
    Timer timer = null;
    


    
    
    public void setTimer(int delay){
        timer = new Timer(delay, this);
        timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
              move();
    }
    
    public void move() {
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
