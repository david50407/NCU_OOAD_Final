package tw.davy.umleditor.uml.renderer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import tw.davy.umleditor.constant.Constant;
import tw.davy.umleditor.uml.Direction;
import tw.davy.umleditor.uml.UMLObject;
import tw.davy.umleditor.util.UMLConnectorHelper;

/**
 * @author Davy
 */
public abstract class CachedRenderer<T extends UMLObject, C> implements Renderer<T> {
    private final HashMap<C, BufferedImage> sCaches = new HashMap<>();

    @Override
    public void render(final T umlObject, final Graphics2D g2d) {
        final C cacheKey = getCacheKey(umlObject);

        BufferedImage cache = sCaches.get(cacheKey);
        if (cache == null) {
            cache = renderBufferedImage(umlObject);
            sCaches.put(cacheKey, cache);
        }

        if (umlObject.isSelected()) {
            for (final Direction dir : Direction.values()) {
                final Point centerPoint = UMLConnectorHelper.getConnectorCenterPoint(umlObject, dir);
                g2d.fillRect(
                        centerPoint.x - Constant.CONNECTOR_HALF_SIZE,
                        centerPoint.y - Constant.CONNECTOR_HALF_SIZE,
                        Constant.CONNECTOR_HALF_SIZE * 2,
                        Constant.CONNECTOR_HALF_SIZE * 2);
            }
        }

        g2d.drawImage(cache, umlObject.getX(), umlObject.getY(), null);
    }

    protected C getCacheKey(final T umlObject) {
        throw new RuntimeException("Operation not implemented.");
    }

    protected BufferedImage renderBufferedImage(final T umlObject) {
        throw new RuntimeException("Operation not implemented.");
    }
}
