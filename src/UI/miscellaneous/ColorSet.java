package UI.miscellaneous;
import java.awt.Color;

/**
 * Set of colors for the level.
 * @author Artemii Kolomiichuk
 */
public class ColorSet {
    public Color background;
    public Color filling;
    public Color outline;
    public Color highlight;
    
    public Color health;
    public Color timerAlert;
    /**
     * Set of colors for neptun design.
     */
    public ColorSet neptun(){
        ColorSet set = new ColorSet();
        set.background = new Color(23,63,31);
        set.filling = new Color(78,159,61);
        set.outline = new Color(42,113,56);
        set.highlight = new Color(216,233,168);
        set.health = new Color(159,11,25);
        set.timerAlert = new Color(161,15,19);
        return set;
    }
}
