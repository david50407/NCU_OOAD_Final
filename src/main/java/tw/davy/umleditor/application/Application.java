package tw.davy.umleditor.application;

import org.jetbrains.annotations.NotNull;

import java.awt.Canvas;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import tw.davy.umleditor.graphics.UMLCanvas;
import tw.davy.umleditor.graphics.Window;

import static tw.davy.umleditor.constant.Constant.LOGGER_NAME;

/**
 * Created by Davy on 2016/10/20.
 */
public class Application {
    static private Application sApplication;

    private final Logger mLogger;
    private final FontMetrics mFontMetrics;
    private Window mWindow;

    static public Application getInstance() {
        if (sApplication == null)
            sApplication = new Application();

        return sApplication;
    }

    private Application() {
        mLogger = Logger.getLogger(LOGGER_NAME);
        mLogger.setLevel(Level.INFO);

        final BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        mFontMetrics = bufferedImage.getGraphics().getFontMetrics();
    }

    public void run() {
        mWindow = new Window();
        mWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                mWindow.quit();
            }
        });
        mWindow.setVisible(true);
    }

    @NotNull
    public Logger getLogger() {
        return mLogger;
    }

    @NotNull
    public FontMetrics getFontMetrics() {
        return mFontMetrics;
    }

    public URL getResourceURL(final String path) {
        return this.getClass().getResource("/" + path);
    }
}
