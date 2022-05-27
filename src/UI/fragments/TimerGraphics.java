package UI.fragments;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;

/**
 * Countdown timer
 * @author Artemii Kolomiichuk
 */
public class TimerGraphics extends JPanel implements ActionListener{
    Timer timer = new Timer(1000, this);
    int time = 0;
    JLabel timerText;
    Color base;
    Color alert;
    
    /**
     * Timer
     * @param time time in seconds
     * @param base base color
     * @param alert alert color
     */
    public TimerGraphics(int time, Color base, Color alert){
        super();
        this.base = base;
        this.alert = alert;
        init(time);
    }
    
    private void init(int time){
        this.time = time;
        setBounds(0, 0, 500, 500);
        timer.start();
        timerText = new JLabel("");
        timerText.setForeground(base);
        add(timerText);
    }
    
    private void resetTimerText(){
        time--;
        if(time < 0){
            onTimeUp();
            timer.stop();
            timerText.setText("");
        }
        else{
            String seconds = Integer.toString(time%60);
            if(seconds.length() < 2){
                timerText.setForeground(alert);
                seconds = 0 + seconds;
            } 
            timerText.setText(time/60 + ":" + seconds);
        }
    }
    
    private void onTimeUp(){
        System.out.println("time is up");
    }
    
    /**
     * Runs every second
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        resetTimerText();
    }
}
