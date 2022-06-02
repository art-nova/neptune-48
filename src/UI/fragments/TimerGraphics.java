package UI.fragments;

import UI.LevelGraphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
import models.App;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLayeredPane;

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
        time++;
        LevelGraphics level = App.getLevel();
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(level.sizes.timerWidth, level.sizes.timerHeight));
        this.time = time;
        JLabel image = new JLabel(App.getLevel().images.timer);
        image.setBounds(0,-10,level.sizes.timerWidth, level.sizes.timerHeight + 10);
        timer.start();
        timerText = new JLabel("");
        timerText.setFont(new Font("Rubik", Font.BOLD, 38));
        timerText.setForeground(base);
        timerText.setBounds(23,-5,level.sizes.timerWidth, level.sizes.timerHeight);
        pane.add(timerText, 0);
        pane.add(image, 100);
        add(pane);
        setBackground(level.colors.background);
        resetTimerText();
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
                if(time/60 < 1){
                    timerText.setForeground(alert);
                }                
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
