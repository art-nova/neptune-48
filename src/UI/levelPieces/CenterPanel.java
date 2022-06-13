package UI.levelPieces;

import javax.swing.JLayeredPane;

import UI.LevelGraphics;
import game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;

import models.App;
import game.utils.GamePanelGraphics;

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
        //add(new Board());
        try
	{
	    add(new GamePanel(4, 4, new HashSet<>(), new HashSet<>(),
                new GamePanelGraphics("resources/images/neptun"), 5000, 1, 120, 5));
	}
	catch (IOException e){}
        add(new Attack());
    }
        
}
