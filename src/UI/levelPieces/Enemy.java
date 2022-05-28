package UI.levelPieces;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.App;

/**
 * Enemy image.
 * @author Artemii Kolomiichuk
 */
public class Enemy extends JPanel{
        
    public Enemy(int index){
        setPreferredSize(new Dimension (App.getLevel().sizes.enemyWidth, App.getLevel().sizes.enemyHeight));
        add(new JLabel(App.getLevel().images.enemy[index]));
        setBackground(new Color(0,0,0,0));
    }
        
}
