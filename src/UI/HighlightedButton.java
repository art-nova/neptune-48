package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Highlighted button.
 * @author Artemii Kolomiichuk
 */
public class HighlightedButton extends JLayeredPane{
    
    public FilledBox body;
    public GradientBox gradient;
    
    /**
     * Creates a new button.
     * @param width width of the button.
     * @param height height of the button.
     * @param backColor background color of the button.
     * @param highlightColor highlight gradient color.
     * @param filling component on the button.
     */
    public HighlightedButton(int width, int height, Color backColor, Color highlightColor, Component filling){
        body = new FilledBox(backColor);
        body.setBounds(0, 0, width, height);
        filling.setBounds(0, 0, width, height);
        
        FilledBox highlight = new FilledBox(highlightColor);
        highlight.setBounds(0, height-5, width, 5);
        
        gradient = new GradientBox(width, height,
                highlightColor,
                Utilities.withZeroAlpha(highlightColor),
                GradientBox.directions.BOTTTOM_TO_TOP);
        
        gradient.setBounds(0,0,width,height);
        gradient.setBackground(new Color(255,255,255,0));
        
        body.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    highlight.setVisible(true);
                    gradient.setVisible(true);
        }});
        gradient.addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent e) {
                    highlight.setVisible(false);
                    gradient.setVisible(false);
        }});
        
        highlight.setVisible(false);
        gradient.setVisible(false);
        
        add(filling, 0);
        add(body, 5);
        add(highlight, 0);
        add(gradient, 0);
    }
}
