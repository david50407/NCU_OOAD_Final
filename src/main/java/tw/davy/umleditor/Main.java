package tw.davy.umleditor;

import tw.davy.umleditor.application.Application;

import java.util.logging.Logger;

import javax.swing.UIManager;

import static tw.davy.umleditor.constant.Constant.LOGGER_NAME;

/**
 * Created by Davy on 2016/10/20.
 */
public class Main {
    static public void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Application.getInstance().run();
        } catch (Exception e) {
            final Logger logger = Logger.getLogger(LOGGER_NAME);
            logger.warning("Unhandled Exception");
            e.printStackTrace();
        }
    }
}
