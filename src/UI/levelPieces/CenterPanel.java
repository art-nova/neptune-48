package UI.levelPieces;

import javax.swing.JLayeredPane;

import javax.swing.*;
import java.awt.*;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class CenterPanel extends JLayeredPane{

    public CenterPanel(){
        setSize(App.getLevel().sizes.boardSize, App.getLevel().sizes.windowHeight);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(App.getLevel().sizes.boardSize, App.getLevel().sizes.centerPanelOffset)));
        
    }
        
}
