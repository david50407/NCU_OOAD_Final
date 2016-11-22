package tw.davy.umleditor.uml.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import tw.davy.umleditor.uml.Composite;

/**
 * @author Davy
 */
public class CompositeRenderer implements Renderer<Composite> {
    static private final float STROKE_DASHES[] = { 10.0f };
    static private final BasicStroke DASHED_STROKE = new BasicStroke(
            1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND,
            10.0f,
            STROKE_DASHES,
            0.0f
    );

    @Override
    public void render(Composite composite, Graphics2D g2d) {
        final Rectangle rect = composite.getRect();

        if (composite.isSelected()) {
            final Color originalColor = g2d.getColor();
            final Stroke originalStroke = g2d.getStroke();

            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.setStroke(DASHED_STROKE);
            g2d.drawRect(rect.x, rect.y, rect.width, rect.height);

            g2d.setColor(originalColor);
            g2d.setStroke(originalStroke);
        }
    }
}
