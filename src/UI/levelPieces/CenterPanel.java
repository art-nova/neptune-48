package UI.levelPieces;

import javax.swing.JLayeredPane;

import UI.LevelGraphics;

import javax.swing.*;
import java.awt.*;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class CenterPanel extends JLayeredPane{

    public CenterPanel(){
        LevelGraphics level = App.getLevel();
        setSize(level.sizes.boardSize, level.sizes.windowHeight);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(level.sizes.boardSize, level.sizes.centerPanelOffset)));
        CenterTop healthTimer = new CenterTop();
        healthTimer.setBackground(new Color(0,0,0,0));
        add(healthTimer);
        add(Box.createRigidArea(new Dimension(level.sizes.boardSize, level.sizes.centerPanelOffset)));
        add(new Enemy(0));
        add(Box.createRigidArea(new Dimension(level.sizes.boardSize, level.sizes.centerPanelOffset)));
        add(new Board());
        add(new Attack());
    }
        
}
