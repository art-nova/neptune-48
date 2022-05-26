/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
/**
 *
 * @author temak
 */
public class TimerGraphics extends JPanel implements ActionListener{
    Timer timer = new Timer(1000, this);
    int time = 0;
    JLabel timerText;
    
    public TimerGraphics(int time){
        super();
        init(time);
    }
    
    public void init(int time){
        this.time = time;
        setBounds(0, 0, 500, 500);
        timer.start();
        timerText = new JLabel("");
        add(timerText);
    }
    
    void resetTimerText(){
        time--;
        if(time <= 0){
            onTimeUp();
            timer.stop();
            timerText.setText("");
        }
        else{
            String seconds = Integer.toString(time%60);
        if(seconds.length() < 2)
            seconds = 0 + seconds;
        timerText.setText(time/60 + ":" + seconds);
        }
    }
    
    void onTimeUp(){
        System.out.println("time is up");
    }
    public void actionPerformed(ActionEvent e) {
        resetTimerText();
    }
}
