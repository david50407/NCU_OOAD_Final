package tw.davy.umleditor.uml.renderer;

import java.awt.Graphics2D;

import tw.davy.umleditor.uml.UMLObject;

/**
 * @author Davy
 */
public interface Renderer<T extends UMLObject> {
    void render(final T obj, final Graphics2D g2d);
}
