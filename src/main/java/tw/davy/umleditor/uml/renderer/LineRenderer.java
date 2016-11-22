package tw.davy.umleditor.uml.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import tw.davy.umleditor.constant.Constant;
import tw.davy.umleditor.uml.Line;
import tw.davy.umleditor.uml.UMLObject;
import tw.davy.umleditor.util.UMLConnectorHelper;

/**
 * @author Davy
 */
public class LineRenderer implements Renderer<Line> {
    @Override
    public void render(Line line, Graphics2D g2d) {
        final UMLObject startAt = line.getStartObject();
        final UMLObject endAt = line.getEndObject();
        if (startAt == null || endAt == null)
            return;

        final Point startPos = UMLConnectorHelper.getConnectorPoint(startAt, line.getStartDirection());
        final Point endPos = UMLConnectorHelper.getConnectorPoint(endAt, line.getEndDirection());
        g2d.drawLine(startPos.x, startPos.y, endPos.x, endPos.y);

        final Polygon polygon = new Polygon();
        switch (line.getType()) {
            case GENERALIZATION: {
                polygon.addPoint(0, 0);
                polygon.addPoint(Constant.GENERALIZATION_LINE_ARROW_SIZE, Constant.GENERALIZATION_LINE_ARROW_SIZE);
                polygon.addPoint(Constant.GENERALIZATION_LINE_ARROW_SIZE, -Constant.GENERALIZATION_LINE_ARROW_SIZE);

                break;
            }
            case COMPOSITION: {
                polygon.addPoint(0, 0);
                polygon.addPoint(Constant.COMPOSITION_LINE_ARROW_SIZE, Constant.COMPOSITION_LINE_ARROW_SIZE);
                polygon.addPoint(Constant.COMPOSITION_LINE_ARROW_SIZE * 2, 0);
                polygon.addPoint(Constant.COMPOSITION_LINE_ARROW_SIZE, -Constant.COMPOSITION_LINE_ARROW_SIZE);

                break;
            }
        }
        if (polygon.npoints == 0)
            return;

        final Color originalColor = g2d.getColor();
        final AffineTransform originalTransform = g2d.getTransform();
        final AffineTransform transform = new AffineTransform(originalTransform);
        transform.concatenate(AffineTransform.getTranslateInstance(endPos.x, endPos.y));
        transform.concatenate(AffineTransform.getRotateInstance(
                Math.atan2(startPos.y - endPos.y, startPos.x - endPos.x)
        ));
        g2d.setTransform(transform);

        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(polygon);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(polygon);

        g2d.setColor(originalColor);
        g2d.setTransform(originalTransform);
    }
}
