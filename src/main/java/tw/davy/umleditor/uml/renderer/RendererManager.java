package tw.davy.umleditor.uml.renderer;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import tw.davy.umleditor.uml.Composite;
import tw.davy.umleditor.uml.Klass;
import tw.davy.umleditor.uml.Line;
import tw.davy.umleditor.uml.UMLObject;
import tw.davy.umleditor.uml.Usecase;

/**
 * @author Davy
 */
public class RendererManager {
    static final private Map<Class<? extends UMLObject>, Renderer> sRenderers = new HashMap<>();

    static {
        sRenderers.put(Klass.class, new KlassRenderer());
        sRenderers.put(Usecase.class, new UsecaseRenderer());
        sRenderers.put(Line.class, new LineRenderer());
        sRenderers.put(Composite.class, new CompositeRenderer());
    }

    static public <T extends UMLObject> void render(final T umlObject, final Graphics2D g2d) {
        final Renderer<T> renderer = sRenderers.get(umlObject.getClass());
        if (renderer != null)
            renderer.render(umlObject, g2d);
    }
}
