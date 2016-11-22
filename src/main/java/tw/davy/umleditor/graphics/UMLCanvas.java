package tw.davy.umleditor.graphics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tw.davy.umleditor.Pair;
import tw.davy.umleditor.graphics.Toolbar.Type;
import tw.davy.umleditor.uml.Composite;
import tw.davy.umleditor.uml.Direction;
import tw.davy.umleditor.uml.Klass;
import tw.davy.umleditor.uml.Line;
import tw.davy.umleditor.uml.LineType;
import tw.davy.umleditor.uml.ManualMovable;
import tw.davy.umleditor.uml.UMLObject;
import tw.davy.umleditor.uml.Usecase;
import tw.davy.umleditor.uml.renderer.RendererManager;
import tw.davy.umleditor.util.Reversed;
import tw.davy.umleditor.util.UMLConnectorHelper;

/**
 * @author Davy
 */
public class UMLCanvas extends JPanel implements MouseListener, MouseMotionListener {
    static private final MouseAdapter DUMMY_MOUSE_LISTENER = new MouseAdapter() {};
    static private final float STROKE_DASHES[] = { 10.0f };
    static private final BasicStroke DASHED_STROKE = new BasicStroke(
            1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND,
            10.0f,
            STROKE_DASHES,
            0.0f
    );
    private Type mCurrentTool = Type.SELECT;
    private ArrayList<UMLObject> mUMLObjects = new ArrayList<>();
    private MouseAdapter mContinusEventListener = DUMMY_MOUSE_LISTENER;
    private Pair<Point, Point> mSupportLine;
    private Rectangle mSupportRectangle;

    public UMLCanvas() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void prepareToRepaint() {
        validate();
        repaint();
    }

    private void add(final UMLObject umlObject) {
        mUMLObjects.add(umlObject);
        prepareToRepaint();
    }

    private void remove(final UMLObject umlObject) {
        mUMLObjects.remove(umlObject);
        prepareToRepaint();
    }

    @Nullable
    private UMLObject findObjectByPos(final int x, final int y) {
        return findObjectByPos(x, y, false);
    }

    @Nullable
    private UMLObject findObjectByPos(final int x, final int y, final boolean skipComposites) {
        for (final UMLObject obj : Reversed.of(mUMLObjects)) {
            if (obj instanceof Line)
                continue;
            if (skipComposites && obj instanceof Composite)
                continue;
            if (obj.getRect().contains(x, y))
                return obj;
        }

        return null;
    }

    @NotNull
    private List<UMLObject> findObjectsByRect(final Rectangle rect) {
        final List<UMLObject> objects = new ArrayList<>();

        for (final UMLObject obj : Reversed.of(mUMLObjects)) {
            if (obj instanceof Line || obj.getParentObject() != null)
                continue;
            if (rect.contains(obj.getRect()))
                objects.add(obj);
        }

        return objects;
    }

    private void setSupportLine() {
        mSupportLine = null;
        prepareToRepaint();
    }

    private void setSupportLine(final Point from, final Point to) {
        mSupportLine = Pair.of(from, to);
        prepareToRepaint();
    }

    private void setSupportRectangle() {
        mSupportRectangle = null;
        prepareToRepaint();
    }

    private void setSupportRectangle(final Point from, final Point to) {
        mSupportRectangle = new Rectangle(from);
        mSupportRectangle.add(to);
        prepareToRepaint();
    }

    private Direction calculateDirectionOfObjectSide(final UMLObject object, final Point point) {
        final Rectangle rect = object.getRect();
        if (!rect.contains(point))
            return null;

        final double dx = (point.x - rect.x) * 2.0 / rect.width - 1;
        final double dy = (point.y - rect.y) * 2.0 / rect.height - 1;

        if (Math.abs(dx) > Math.abs(dy)) { // East or West
            if (dx > 0)
                return Direction.EAST;
            else
                return Direction.WEST;
        } else { // North or South
            if (dy > 0)
                return Direction.SOUTH;
            else
                return Direction.NORTH;
        }
    }

    private void focusOnObject(@Nullable final UMLObject targetObject) {
        mUMLObjects.forEach(umlObject -> umlObject.setSelected(false));

        if (targetObject != null)
            targetObject.setSelected(true);

        prepareToRepaint();
    }

    private void focusOnObjects(final List<UMLObject> targetObjects) {
        mUMLObjects.forEach(umlObject -> umlObject.setSelected(false));
        targetObjects.forEach(umlObject -> umlObject.setSelected(true));

        prepareToRepaint();
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        switch (mCurrentTool) {
            case SELECT: {
                final UMLObject umlObject = findObjectByPos(mouseEvent.getX(), mouseEvent.getY());
                focusOnObject(umlObject);

                break;
            }
            case CLASS: {
                final Klass klass = new Klass();
                klass.moveTo(mouseEvent.getX(), mouseEvent.getY());

                add(klass);
                break;
            }
            case USE_CASE: {
                final Usecase usecase = new Usecase();
                usecase.moveTo(mouseEvent.getX(), mouseEvent.getY());

                add(usecase);
                break;
            }
        }
    }

    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        switch (mCurrentTool) {
            case SELECT: {
                final UMLObject target = findObjectByPos(mouseEvent.getX(), mouseEvent.getY());
                if (target == null) { // Multiple-selecting
                    final Point from = mouseEvent.getPoint();

                    mContinusEventListener = new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent mouseEvent) {
                            final Rectangle rect = new Rectangle(from);
                            rect.add(mouseEvent.getPoint());
                            focusOnObjects(findObjectsByRect(rect));

                            mContinusEventListener = DUMMY_MOUSE_LISTENER;
                            setSupportRectangle();
                        }

                        @Override
                        public void mouseDragged(MouseEvent mouseEvent) {
                            setSupportRectangle(from, mouseEvent.getPoint());
                        }
                    };
                } else { // Move
                    focusOnObject(target);
                    final Point mouseOffset = new Point(mouseEvent.getPoint());
                    mouseOffset.translate(-target.getX(), -target.getY());

                    mContinusEventListener = new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent mouseEvent) {
                            mContinusEventListener = DUMMY_MOUSE_LISTENER;
                        }

                        @Override
                        public void mouseDragged(MouseEvent mouseEvent) {
                            final Point position = new Point(mouseEvent.getPoint());
                            position.translate(-mouseOffset.x, -mouseOffset.y);

                            if (target instanceof ManualMovable)
                                ((ManualMovable) target).moveTo(position.x, position.y);

                            prepareToRepaint();
                        }
                    };
                }

                break;
            }
            case ASSOCIATION_LINE:
            case GENERALIZATION_LINE:
            case COMPOSITION_LINE: {
                final UMLObject start = findObjectByPos(mouseEvent.getX(), mouseEvent.getY(), true);
                if (start == null)
                    break;

                final LineType type;
                if (mCurrentTool == Type.ASSOCIATION_LINE)
                    type = LineType.ASSOCIATION;
                else if (mCurrentTool == Type.GENERALIZATION_LINE)
                    type = LineType.GENERALIZATION;
                else // COMPOSITION_LINE
                    type = LineType.COMPOSITION;

                final Direction startDir = calculateDirectionOfObjectSide(start, mouseEvent.getPoint());
                final Point from = UMLConnectorHelper.getConnectorPoint(start, startDir);

                mContinusEventListener = new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        final UMLObject end = findObjectByPos(mouseEvent.getX(), mouseEvent.getY(), true);
                        if (end != null && end != start) {
                            final Line line = new Line(type);
                            line.setStartObject(start);
                            line.setStartDirection(startDir);
                            line.setEndObject(end);
                            line.setEndDirection(calculateDirectionOfObjectSide(end, mouseEvent.getPoint()));

                            add(line);
                        }

                        mContinusEventListener = DUMMY_MOUSE_LISTENER;
                        setSupportLine();
                    }

                    @Override
                    public void mouseDragged(MouseEvent mouseEvent) {
                        setSupportLine(from, mouseEvent.getPoint());
                    }
                };
                mContinusEventListener.mouseDragged(mouseEvent);

                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mContinusEventListener.mouseReleased(mouseEvent);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mContinusEventListener.mouseDragged(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        final Graphics2D g2d = (Graphics2D) graphics;

        mUMLObjects.forEach((umlObject -> {
            RendererManager.render(umlObject, g2d);
        }));
        if (mSupportLine != null) {
            final Stroke originalStroke = g2d.getStroke();
            g2d.setStroke(DASHED_STROKE);
            g2d.drawLine(
                    mSupportLine.first.x, mSupportLine.first.y,
                    mSupportLine.second.x, mSupportLine.second.y
            );
            g2d.setStroke(originalStroke);
        }

        if (mSupportRectangle != null) {
            final Color originalColor = g2d.getColor();
            g2d.setColor(new Color(200, 200, 200, 80));
            g2d.fillRect(
                    mSupportRectangle.x,
                    mSupportRectangle.y,
                    mSupportRectangle.width,
                    mSupportRectangle.height
            );
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(
                    mSupportRectangle.x,
                    mSupportRectangle.y,
                    mSupportRectangle.width,
                    mSupportRectangle.height
            );
            g2d.setColor(originalColor);
        }
    }

    public void setCurrentTool(final Type toolType) {
        mCurrentTool = toolType;
    }

    public void groupSelected() {
        final List<UMLObject> objects = new ArrayList<>();
        mUMLObjects.forEach(umlObject -> {
            if (umlObject.isSelected() && umlObject.getParentObject() == null)
                objects.add(umlObject);
        });

        if (objects.size() < 2)
            return;

        final Composite composite = new Composite();
        objects.forEach(composite::add);
        add(composite);
        focusOnObject(composite);
    }

    public void ungroupSelected() {
        final List<Composite> topComposites = new ArrayList<>();
        mUMLObjects.forEach(umlObject -> {
            if (umlObject instanceof Composite && umlObject.isSelected() && umlObject.getParentObject() == null)
                topComposites.add((Composite) umlObject);
        });

        if (topComposites.isEmpty())
            return;

        topComposites.forEach(composite -> {
            composite.clear();
            remove(composite);
        });
    }

    public void changeSelectedObjectName() {
        final List<UMLObject> topObjects = new ArrayList<>();
        mUMLObjects.forEach(umlObject -> {
            if (umlObject.isSelected() && umlObject.getParentObject() == null)
                topObjects.add(umlObject);
        });

        if (topObjects.size() != 1)
            return;

        final UMLObject target = topObjects.get(0);
        if (target instanceof Klass) {
            final Klass klass = (Klass) target;
            final String name = JOptionPane.showInputDialog(
                    null,
                    "New name for: \"" + klass.getName() + "\"",
                    "Change Object Name",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (name == null)
                return;

            klass.setName(name);
            prepareToRepaint();
        } else if (target instanceof Usecase) {
            final Usecase usecase = (Usecase) target;
            final String name = JOptionPane.showInputDialog(
                    null,
                    "New name for: \"" + usecase.getName() + "\"",
                    "Change Object Name",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (name == null)
                return;

            usecase.setName(name);
            prepareToRepaint();
        }
    }
}
