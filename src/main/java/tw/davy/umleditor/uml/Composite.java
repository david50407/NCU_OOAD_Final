package tw.davy.umleditor.uml;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davy
 */
public class Composite extends UMLObjectBase implements ManualMovable {
    private final List<UMLObject> mUMLObjects = new ArrayList<>();

    public void add(final UMLObject umlObject) {
        mUMLObjects.add(umlObject);
        umlObject.setParentObject(this);
    }

    public void clear() {
        mUMLObjects.forEach(umlObject -> umlObject.setParentObject(null));
        mUMLObjects.clear();
    }

    @Override
    public Rectangle getRect() {
        if (mUMLObjects.size() == 0)
            return new Rectangle();

        final Rectangle result = new Rectangle(mUMLObjects.get(0).getRect());
        mUMLObjects.forEach(umlObject -> result.add(umlObject.getRect()));

        return result;
    }

    @Override
    public int getX() {
        return getRect().x;
    }

    @Override
    public int getY() {
        return getRect().y;
    }

    @Override
    public int getWidth() {
        return getRect().width;
    }

    @Override
    public int getHeight() {
        return getRect().height;
    }

    @Override
    public void setSelected(final boolean isSelected) {
        super.setSelected(isSelected);
        mUMLObjects.forEach(umlObject -> umlObject.setSelected(isSelected));
    }

    @Override
    public void moveTo(final int x, final int y) {
        final Rectangle originalRect = getRect();
        final int offsetX = x - originalRect.x;
        final int offsetY = y - originalRect.y;

        mUMLObjects.forEach(umlObject -> {
            if (umlObject instanceof ManualMovable)
                ((ManualMovable) umlObject).moveTo(umlObject.getX() + offsetX, umlObject.getY() + offsetY);
        });
    }
}
