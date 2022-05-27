package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Solid colored box.
 * @author Artemii Kolomiichuk
 */
public class FilledBox extends JPanel{
    /**
     * Creates a new box.
     * @param color color of the box.
     */
    public FilledBox (Color color){
        super();
        setBackground(color);
    }
}
