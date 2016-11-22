package tw.davy.umleditor.uml.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import tw.davy.umleditor.Pair;
import tw.davy.umleditor.application.Application;
import tw.davy.umleditor.constant.Constant;
import tw.davy.umleditor.uml.Klass;
import tw.davy.umleditor.uml.Line;
import tw.davy.umleditor.util.UMLConnectorHelper;

/**
 * @author Davy
 */
public class KlassRenderer extends CachedRenderer<Klass, Pair<String, Pair<Integer, Integer>>> {
    static public final int TEXT_PADDING = 8;
    static public final int BOX_HEIGHT = 30;

    @Override
    protected Pair<String, Pair<Integer, Integer>> getCacheKey(final Klass klass) {
        return Pair.of(klass.getName(), Pair.of(klass.getX(), klass.getY()));
    }

    @Override
    protected BufferedImage renderBufferedImage(final Klass klass) {
        final Rectangle rect = klass.getRect();

        final BufferedImage bufferedImage = new BufferedImage(rect.width + 1, rect.height + 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        final Rectangle nameRect = measureNameRect(klass);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, rect.width, rect.height);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, rect.width, rect.height);
        g2d.drawString(klass.getName(), TEXT_PADDING, TEXT_PADDING + nameRect.height * 3 / 4);
        g2d.drawLine(
                0, nameRect.height + TEXT_PADDING * 2,
                rect.width, nameRect.height + TEXT_PADDING * 2
        );
        g2d.drawLine(
                0, nameRect.height + TEXT_PADDING * 2 + BOX_HEIGHT,
                rect.width, nameRect.height + TEXT_PADDING * 2 + BOX_HEIGHT
        );

        g2d.dispose();

        return bufferedImage;
    }

    private Rectangle measureNameRect(final Klass klass) {
        final Application app = Application.getInstance();
        final FontMetrics fontMetrics = app.getFontMetrics();
        final int width = fontMetrics.stringWidth(klass.getName());
        final int height = fontMetrics.getHeight();

        return new Rectangle(width, height);
    }
}
