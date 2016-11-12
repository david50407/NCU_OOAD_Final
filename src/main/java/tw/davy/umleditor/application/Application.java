package tw.davy.umleditor.application;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import static tw.davy.umleditor.constant.Constant.LOGGER_NAME;

/**
 * Created by Davy on 2016/10/20.
 */
public class Application {
    static private Application sApplication;

    private final Logger mLogger;

    static public Application getInstance() {
        if (sApplication == null)
            sApplication = new Application();

        return sApplication;
    }

    private Application() {
        mLogger = Logger.getLogger(LOGGER_NAME);
        mLogger.setLevel(Level.INFO);
    }

    public void run() {

    }

    @NotNull
    public Logger getLogger() {
        return mLogger;
    }
}
