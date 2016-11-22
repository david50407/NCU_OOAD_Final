package tw.davy.umleditor.uml;

import tw.davy.umleditor.application.Application;
import tw.davy.umleditor.uml.renderer.UsecaseRenderer;

/**
 * @author Davy
 */
public class Usecase extends UMLObjectBase implements ManualMovable {
    private String mName = "Use Case";

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    @Override
    public int getWidth() {
        final Application app = Application.getInstance();
        final int nameWidth = app.getFontMetrics().stringWidth(getName());

        return nameWidth + 2 * UsecaseRenderer.TEXT_PADDING;
    }

    @Override
    public int getHeight() {
        final Application app = Application.getInstance();
        final int nameHeight = app.getFontMetrics().getHeight();

        return nameHeight + 2 * UsecaseRenderer.TEXT_PADDING;
    }

    @Override
    public void moveTo(final int x, final int y) {
        mX = x;
        mY = y;
    }
}
