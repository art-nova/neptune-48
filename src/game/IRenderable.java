package game;

import java.awt.*;

/**
 * Interface that describes custom objects able to render on a Graphics2D object based on their current state.
 *
 * @author Artem Novak
 */
public interface IRenderable {

    /**
     * Renders the object on a Graphics2D instance.
     *
     * @param g2d Graphics2D instance for rendering
     */
    void render(Graphics2D g2d);
}
