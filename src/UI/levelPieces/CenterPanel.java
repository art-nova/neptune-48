package UI.levelPieces;

import javax.swing.JLayeredPane;

import UI.LevelGraphics;
import UI.UIManager;
import game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
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
        HashMap<String, Integer> obstacleWeights = new HashMap<>();
	    add(new GamePanel(4, 4, null, null, null, obstacleWeights,
                new GamePanelGraphics("resources/images/default", 94, 5), UIManager.getFrame(), 5000, 0, 120, 5,
                10, 30, GamePanel.GAME_MODE_ATTACK));
	}
	catch (IOException e){e.printStackTrace();}
        add(new Attack());
    }
        
}
