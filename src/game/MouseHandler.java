package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class that implements methods for registering mouse moving onto a component and left button presses.
 *
 * @author Artem Novak
 */
public class MouseHandler extends MouseAdapter {
    private boolean mouseOn;
    private boolean mousePressed;

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

    public boolean isMouseOn() {
        return mouseOn;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Tells the handler that last mouse press has been resolved and does not need processing anymore.
     */
    public void resetMousePressed() {
        this.mousePressed = false;
    }
}
