import game.GamePanel;
import utils.LevelGraphicsManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class GameplayDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Gameplay Demo");
        try {
            frame.add(new GamePanel(4, 5, new HashSet<>(), new HashSet<>(),
                    new LevelGraphicsManager(
                            "/images/tiles_testing",
                            "lorem ipsum",
                            "lorem ipsum",
                            "lorem ipsum",
                            "lorem ipsum",
                            Color.red,
                            50, 50
                    )));
        } catch (Exception e) {System.err.println(e.getMessage());}
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
