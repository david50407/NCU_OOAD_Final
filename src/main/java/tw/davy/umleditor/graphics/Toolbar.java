package tw.davy.umleditor.graphics;

import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import tw.davy.umleditor.application.Application;

/**
 * @author Davy
 */
public class Toolbar extends JToolBar {
    private final ArrayList<JToggleButton> mButtons = new ArrayList<>();
    private final ToolbarStateListener mListener;

    public enum Type {
        SELECT,
        ASSOCIATION_LINE,
        GENERALIZATION_LINE,
        COMPOSITION_LINE,
        CLASS,
        USE_CASE
    }

    public Toolbar(final ToolbarStateListener listener) {
        super();

        mListener = listener;
        buildToolbar();
        setFloatable(false);
        setOrientation(VERTICAL);
    }

    private void buildToolbar() {
        mButtons.add(buildButton("Select", "select.png"));
        mButtons.add(buildButton("Association Line", "association_line.png"));
        mButtons.add(buildButton("Generalization Line", "generalization_line.png"));
        mButtons.add(buildButton("Composition Line", "composition_line.png"));
        mButtons.add(buildButton("Class", "class.png"));
        mButtons.add(buildButton("Use Case", "usecase.png"));

        mButtons.forEach(this::add);
        onButtonToggle(mButtons.get(0));
    }

    private JToggleButton buildButton(final String name,
                                      final String imagePath) {
        final URL image = Application.getInstance().getResourceURL(imagePath);
        final JToggleButton button = new JToggleButton();

        button.setToolTipText(name);
        button.addActionListener((actionEvent -> onButtonToggle(button)));
        if (image != null)
            button.setIcon(new ImageIcon(image, name));
        else
            button.setText(name);

        return button;
    }

    private void onButtonToggle(JToggleButton toggledButton) {
        mButtons.forEach((button) -> button.setSelected(button == toggledButton));

        mListener.onStateChanged(Type.values()[mButtons.indexOf(toggledButton)]);
    }
    interface ToolbarStateListener {
        void onStateChanged(Type state);
    }
}
