package UI.levelPieces;

import javax.swing.*;
import java.awt.*;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class LeftBar extends JLayeredPane{
        
    public LeftBar(){
        setSize(App.getLevel().sizes.barOffset * 2 + App.getLevel().sizes.buttonSize, App.getLevel().sizes.windowHeight);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createRigidArea(new Dimension(App.getLevel().sizes.barOffset, App.getLevel().sizes.windowHeight)));
        ///
        add(Box.createRigidArea(new Dimension(App.getLevel().sizes.barOffset, App.getLevel().sizes.windowHeight)));
    }
        
}
