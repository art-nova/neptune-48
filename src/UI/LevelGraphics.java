package UI;

import UI.levelPieces.CenterPanel;
import UI.levelPieces.LeftBar;
import UI.levelPieces.RightBar;
import UI.miscellaneous.ColorSet;
import UI.miscellaneous.ImageSet;
import UI.miscellaneous.SizeSet;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Level JPanel
 * @author Artemii Kolomiichuk
 */
public class LevelGraphics extends JPanel{

    public SizeSet sizes;
    public ColorSet colors;
    public ImageSet images;
    public int time = 100;
    
    public LevelGraphics(){
        super();
        sizes = new SizeSet().defaultSizeSet();
        colors = new ColorSet().neptun();
        images = new ImageSet("neptun", this);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBounds(0,0,sizes.windowWidth,sizes.windowHeight);
        setBackground(colors.background);
    }
    
    public void addGraphics(){
        add(new LeftBar());
        add(new CenterPanel());
        add(new RightBar());
    }

}
