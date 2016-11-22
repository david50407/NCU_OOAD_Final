package tw.davy.umleditor.uml;

import java.awt.Rectangle;

/**
 * @author Davy
 */
public class Line extends UMLObjectBase {

    private final LineType mType;
    private UMLObject mStartObject, mEndObject;
    private Direction mStartDir, mEndDir;

    public Line(final LineType type) {
        mType = type;
    }

    public LineType getType() {
        return mType;
    }

    public UMLObject getStartObject() {
        return mStartObject;
    }

    public UMLObject getEndObject() {
        return mEndObject;
    }

    public void setStartObject(final UMLObject object) {
        mStartObject = object;
    }

    public void setEndObject(final UMLObject object) {
        mEndObject = object;
    }

    public Direction getStartDirection() {
        return mStartDir;
    }

    public Direction getEndDirection() {
        return mEndDir;
    }

    public void setStartDirection(final Direction dir) {
        mStartDir = dir;
    }

    public void setEndDirection(final Direction dir) {
        mEndDir = dir;
    }

    @Override
    public Rectangle getRect() {
        final UMLObject startAt = getStartObject();
        final UMLObject endAt = getEndObject();

        if (startAt == null || endAt == null)
            return new Rectangle(1, 1);

        final Rectangle result = startAt.getRect();
        result.add(endAt.getRect());

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
}
