package tw.davy.umleditor.uml;

import tw.davy.umleditor.application.Application;
import tw.davy.umleditor.uml.renderer.KlassRenderer;

/**
 * @author Davy
 */
public class Klass extends UMLObjectBase implements ManualMovable {
    private String mName = "Class";

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

        return nameWidth + 2 * KlassRenderer.TEXT_PADDING;
    }

    @Override
    public int getHeight() {
        final Application app = Application.getInstance();
        final int nameHeight = app.getFontMetrics().getHeight();

        return nameHeight + 2 * KlassRenderer.TEXT_PADDING + KlassRenderer.BOX_HEIGHT * 2;
    }

    @Override
    public void moveTo(final int x, final int y) {
        mX = x;
        mY = y;
    }
}
