package tw.davy.umleditor.uml.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import tw.davy.umleditor.Pair;
import tw.davy.umleditor.application.Application;
import tw.davy.umleditor.constant.Constant;
import tw.davy.umleditor.uml.Line;
import tw.davy.umleditor.uml.Usecase;
import tw.davy.umleditor.util.UMLConnectorHelper;

/**
 * @author Davy
 */
public class UsecaseRenderer extends CachedRenderer<Usecase, Pair<String, Pair<Integer, Integer>>> {
    static public final int TEXT_PADDING = 16;

    @Override
    protected Pair<String, Pair<Integer, Integer>> getCacheKey(final Usecase usecase) {
        return Pair.of(usecase.getName(), Pair.of(usecase.getX(), usecase.getY()));
    }

    @Override
    protected BufferedImage renderBufferedImage(final Usecase usecase) {
        final Rectangle rect = usecase.getRect();

        final BufferedImage bufferedImage = new BufferedImage(rect.width + 1, rect.height + 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillOval(0, 0, rect.width, rect.height);

        g2d.setColor(Color.BLACK);
        g2d.drawOval(0, 0, rect.width, rect.height);
        g2d.drawString(usecase.getName(), TEXT_PADDING, TEXT_PADDING + Application.getInstance().getFontMetrics().getHeight() * 3 / 4);

        return bufferedImage;
    }
}
