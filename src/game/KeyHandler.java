package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Class that implements methods for registering pressed keys by certain NameIDs.
 *
 * @author Artem Novak
 */
public class KeyHandler extends KeyAdapter {
    private String lastPressKey = null;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            lastPressKey = "up";
        }
        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            lastPressKey = "down";
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            lastPressKey = "left";
        }
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            lastPressKey = "right";
        }
        else if (key == KeyEvent.VK_ESCAPE) {
            lastPressKey = "escape";
        }
    }

    public String getLastPressKey() {
        return lastPressKey;
    }

    public void clearLastPress() {
        lastPressKey = null;
    }
}
