import java.awt.Dimension;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import UI.miscellaneous.FilledBox;

public class LevelMenu extends JFrame{

    int width, height;
    JLayeredPane pane;
    public LevelMenu() {
        super("Level");
        this.width = 800;
        this.height = 900;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);

        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(width, height));
        pane.setBounds(0,0,width,height);

        FilledBox box = new FilledBox(Color.BLACK);
        box.setBounds(15,420,120,240);
        pane.add(box);

        add(pane);
    }
}
