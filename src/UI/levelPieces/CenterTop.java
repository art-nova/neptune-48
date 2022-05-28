package UI.levelPieces;

import javax.swing.JPanel;

import java.awt.Dimension;

import UI.LevelGraphics;
import UI.fragments.HealthBar;
import UI.fragments.TimerGraphics;
import models.App;

/**
 * Healthbar and timer.
 * @author Artemii Kolomiichuk
 */
public class CenterTop extends JPanel{

    public CenterTop(){
        LevelGraphics level = App.getLevel();
        setPreferredSize(new Dimension (level.sizes.boardSize, level.sizes.timerHeight));
        add(new HealthBar());
        TimerGraphics timer = new TimerGraphics(level.time, level.colors.highlight, level.colors.timerAlert);
        timer.setLocation(level.sizes.healthWidth + level.sizes.timerHealthOffset,0);
        timer.setPreferredSize(new Dimension(level.sizes.timerWidth, level.sizes.timerHeight));
        System.out.println(getBounds());
        add(timer);
    }
}
