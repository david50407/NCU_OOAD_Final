package tw.davy.umleditor.graphics;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Created by Davy on 2016/10/20.
 */
public class Window extends JFrame {
    private final JMenuBar mMenuBar = new JMenuBar();
    private final UMLCanvas mUMLCanvas = new UMLCanvas();
    private final MenuItemListener mMenuGroupListener;
    private final MenuItemListener mMenuUngroupListener;
    private final MenuItemListener mMenuChangeNameListener;

    public Window() {
        super("UMLEditor");

        setSize(800, 600);
        setResizable(false);

        initMenuBar();
        add(new Toolbar(mUMLCanvas::setCurrentTool), BorderLayout.WEST);
        add(mUMLCanvas);

        mMenuGroupListener = mUMLCanvas::groupSelected;
        mMenuUngroupListener = mUMLCanvas::ungroupSelected;
        mMenuChangeNameListener = mUMLCanvas::changeSelectedObjectName;
    }

    public void quit() {
        dispose();
    }

    private void initMenuBar() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        {
            final JMenuItem fileQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
            fileQuit.addActionListener(actionEvent -> this.quit());

            fileMenu.add(fileQuit);
        }
        final JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        {
            final JMenuItem changeName = new JMenuItem("Change Object Name", KeyEvent.VK_C);
            changeName.addActionListener(actionEvent -> mMenuChangeNameListener.onClick());
            final JMenuItem editGroup = new JMenuItem("Group", KeyEvent.VK_G);
            editGroup.addActionListener(actionEvent -> mMenuGroupListener.onClick());
            final JMenuItem editUngroup = new JMenuItem("Ungroup", KeyEvent.VK_U);
            editUngroup.addActionListener(actionEvent -> mMenuUngroupListener.onClick());

            editMenu.add(changeName);
            editMenu.addSeparator();
            editMenu.add(editGroup);
            editMenu.add(editUngroup);
        }

        mMenuBar.add(fileMenu);
        mMenuBar.add(editMenu);
        setJMenuBar(mMenuBar);
    }

    interface MenuItemListener {
        void onClick();
    }
}
