package tw.davy.umleditor.uml;

import java.awt.Rectangle;

/**
 * @author Davy
 */
public interface UMLObject {
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    Rectangle getRect();

    boolean isSelected();
    void setSelected(boolean selected);

    UMLObject getParentObject();
    void setParentObject(UMLObject umlObject);
}
