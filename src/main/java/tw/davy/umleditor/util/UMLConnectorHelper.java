package tw.davy.umleditor.util;

import java.awt.Point;
import java.awt.Rectangle;

import tw.davy.umleditor.constant.Constant;
import tw.davy.umleditor.uml.Direction;
import tw.davy.umleditor.uml.UMLObject;

/**
 * @author Davy
 */
public class UMLConnectorHelper {
    static public Point getConnectorPoint(final UMLObject object, final Direction direction) {
        final Rectangle rect = object.getRect();

        switch (direction) {
            case EAST:
                return new Point(rect.x + rect.width, rect.y + rect.height / 2);
            case WEST:
                return new Point(rect.x, rect.y + rect.height / 2);
            case NORTH:
                return new Point(rect.x + rect.width / 2, rect.y);
            case SOUTH:
                return new Point(rect.x + rect.width / 2, rect.y + rect.height);
        }

        throw new RuntimeException("Unexpected direction");
    }

    static public Point getConnectorCenterPoint(final UMLObject object, final Direction direction) {
        final Point point = getConnectorPoint(object, direction);

        switch (direction) {
            case EAST:
                return new Point(point.x + Constant.CONNECTOR_HALF_SIZE, point.y);
            case WEST:
                return new Point(point.x - Constant.CONNECTOR_HALF_SIZE, point.y);
            case NORTH:
                return new Point(point.x, point.y - Constant.CONNECTOR_HALF_SIZE);
            case SOUTH:
                return new Point(point.x, point.y + Constant.CONNECTOR_HALF_SIZE);
        }

        throw new RuntimeException("Unexpected direction");
    }
}
