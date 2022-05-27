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
    
    public LevelGraphics(){
        sizes = sizes.defaultSizeSet();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setSize(sizes.windowWidth,sizes.windowHeight);
        add(new LeftBar());
        add(new CenterPanel());
        add(new RightBar());
    }

}
