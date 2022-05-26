/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author temak
 */
public class GradientBox extends JPanel{

    double x, y;
    int width;
    int height;
    Color firstColor;
    Color secondColor;
    int angle;
    directions direction;
    
    public enum directions{
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTTOM_TO_TOP
    }
    
    public GradientBox(int width, int height, Color firstColor, Color secondColor, directions direction){
        this.width = width;
        this.height = height;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.angle = angle;
        this.direction = direction;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(angle), (double)width/2, (double)height/2);
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int x1=0,y1=0,x2=0,y2 = 0;
        switch(direction){
            case LEFT_TO_RIGHT:
                x1=0;
                y1=height/2;
                x2=width;
                y2=height/2;
                break;
            case RIGHT_TO_LEFT:
                x1=width;
                y1=height/2;
                x2=0;
                y2=height/2;
                break;
            case TOP_TO_BOTTOM:
                x1=width/2;
                y1=0;
                x2=width/2;
                y2=height;
                break;
            case BOTTTOM_TO_TOP:
                x1=width/2;
                y1=height;
                x2=width/2;
                y2=0;
                break;
        }
        GradientPaint paint = new GradientPaint(x1,y1, firstColor, x2,y2,secondColor);
        g2d.setPaint(paint);
        g2d.fillRect(0,0, width, height);
        
    }
}
