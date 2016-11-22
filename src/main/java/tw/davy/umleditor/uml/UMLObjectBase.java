package tw.davy.umleditor.uml;

import org.jetbrains.annotations.Nullable;

import java.awt.Rectangle;

/**
 * @author Davy
 */
public abstract class UMLObjectBase implements UMLObject {
    protected int mX = 0, mY = 0;
    protected boolean mIsSelected = false;
    private UMLObject mParentObject;

    @Override
    public int getX() {
        return mX;
    }

    @Override
    public int getY() {
        return mY;
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public void setSelected(final boolean isSelected) {
        mIsSelected = isSelected;
    }

    @Override
    @Nullable
    public UMLObject getParentObject() {
        return mParentObject;
    }

    @Override
    public void setParentObject(final UMLObject parentObject) {
        mParentObject = parentObject;
    }
}
