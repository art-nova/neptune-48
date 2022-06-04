package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class that implements methods for registering mouse moving onto a component and left button presses.
 *
 * @author Artem Novak
 */
public class MouseHandler extends MouseAdapter {
    public boolean mouseOn;
    public boolean mousePressed;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOn = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOn = false;
    }
}
