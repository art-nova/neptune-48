package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Countdown timer
 * @author Artemii Kolomiichuk
 */
public class TimerGraphics extends JPanel implements ActionListener{
    Timer timer = new Timer(1000, this);
    int time = 0;
    JLabel timerText;
    
    /**
     * Timer
     * @param time time in seconds
     */
    public TimerGraphics(int time){
        super();
        init(time);
    }
    
    private void init(int time){
        this.time = time;
        setBounds(0, 0, 500, 500);
        timer.start();
        timerText = new JLabel("");
        add(timerText);
    }
    
    private void resetTimerText(){
        time--;
        if(time <= 0){
            onTimeUp();
            timer.stop();
            timerText.setText("");
        }
        else{
            String seconds = Integer.toString(time%60);
            if(seconds.length() < 2){
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
