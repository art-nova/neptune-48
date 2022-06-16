package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that implements methods for registering pressed keys by certain NameIDs.
 *
 * @author Artem Novak
 */
public class KeyHandler extends KeyAdapter {
    private LinkedList<String> pressedKeys = new LinkedList<>();

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            pressedKeys.add("up");
        }
        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            pressedKeys.add("down");
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            pressedKeys.add("left");
        }
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            pressedKeys.add("right");
        }
        else if (key == KeyEvent.VK_SPACE) {
            pressedKeys.add("space");
        }
        else if (key == KeyEvent.VK_ESCAPE) {
            pressedKeys.add("escape");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            pressedKeys.removeIf(x -> x.equals("up"));
        }
        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            pressedKeys.removeIf(x -> x.equals("down"));
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            pressedKeys.removeIf(x -> x.equals("left"));
        }
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            pressedKeys.removeIf(x -> x.equals("right"));
        }
        else if (key == KeyEvent.VK_SPACE) {
            pressedKeys.removeIf(x -> x.equals("space"));
        }
        else if (key == KeyEvent.VK_ESCAPE) {
            pressedKeys.removeIf(x -> x.equals("escape"));
        }
    }

    /**
     * Determines whether there are any registered pressed keys.
     *
     * @return true if there is at least one registered pressed key
     */
    public boolean isKeyPressed() {
        return !pressedKeys.isEmpty();
    }

    /**
     * Gets the latest registered pressed key
     *
     * @return last pressed key
     */
    public String getLastPressKey() {
        return pressedKeys.peekLast();
    }

    /**
     * Removes last registered pressed key.
     */
    public void clearLastPress() {
        pressedKeys.removeLast();
    }
}
