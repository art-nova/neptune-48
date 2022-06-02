package UI.levelPieces;

import UI.fragments.FilledBox;
import javax.swing.*;
import java.awt.*;
import models.App;

/**
 *
 * @author Artemii Kolomiichuk
 */
public class RightBar extends JPanel{

    public RightBar(){
        setSize(App.getLevel().sizes.barOffset * 2 + App.getLevel().sizes.buttonSize, App.getLevel().sizes.windowHeight);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createRigidArea(new Dimension(App.getLevel().sizes.barOffset, App.getLevel().sizes.windowHeight)));
        JPanel buttonBar = new FilledBox(Color.PINK);
        buttonBar.setPreferredSize(new Dimension(App.getLevel().sizes.buttonSize, App.getLevel().sizes.windowHeight));
        add(buttonBar);
        add(Box.createRigidArea(new Dimension(App.getLevel().sizes.barOffset, App.getLevel().sizes.windowHeight)));
    }
        
}
