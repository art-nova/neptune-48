/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resizableinterface;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author temak
 */
public class UpdatedFrame extends JFrame implements ActionListener{
    Timer timer = new Timer(20, this);
    
    public UpdatedFrame(){
        super();
        setTimer();
    }
    
    public UpdatedFrame(String title){
        super(title);
        setTimer();
    }
    
    public void setTimer(){
        //timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
        System.out.print("uff");
        revalidate();
        repaint();      
    }
}
