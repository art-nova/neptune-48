package UI.fragments;

import javax.swing.JLayeredPane;

import UI.LevelGraphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import models.App;

/**
 * HealthBar graphics.
 * @author Artemii Kolomiichuk
 */
public class HealthBar extends JPanel{

    public HealthBar(){
        LevelGraphics level = App.getLevel();
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension (level.sizes.healthWidth, level.sizes.healthHeight));
        
        setBounds(0, 0, level.sizes.healthWidth, level.sizes.healthHeight);
        setPreferredSize(new Dimension(level.sizes.healthWidth, level.sizes.healthHeight));
        FilledBox back = new FilledBox(level.colors.outline);
        FilledBox filling = new FilledBox(level.colors.filling);
        back.setBounds(0, 0, level.sizes.healthWidth, level.sizes.healthHeight);
        filling.setBounds(level.sizes.healthOutlineWidth, level.sizes.healthOutlineWidth, level.sizes.healthWidth - 2 * level.sizes.healthOutlineWidth, level.sizes.healthHeight - 2 * level.sizes.healthOutlineWidth);
        pane.add(back, 15);
        pane.add(filling, 500);
        add(pane);
    }
    

}
