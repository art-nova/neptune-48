package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Class that implements methods for registering pressed keys by certain NameIDs.
 */
public class KeyHandler {
    public String lastPressKey = "";

    /**
     * Constructs a key handler.
     *
     * @param frame JFrame that the key listener is attached to.
     */
    public KeyHandler(JFrame frame) {
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

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
            }

            @Override
            public void keyReleased(KeyEvent e) {
                clearLastPress();
            }
        });
    }

    public void clearLastPress() {
        lastPressKey = "";
    }
}
