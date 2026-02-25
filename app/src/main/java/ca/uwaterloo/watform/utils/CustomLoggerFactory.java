package ca.uwaterloo.watform.utils;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLoggerFactory {

    private static int UID = 0;

    public static Logger make(String fileName, boolean debug) {
        Logger logger = Logger.getLogger("" + UID);
        UID += 1;

        fileName = fileName + ".log";

        logger.setUseParentHandlers(false); // remove ability to access console

        if (!debug) return logger;

        File file = new File(fileName);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try {
            FileHandler fh = new FileHandler(fileName);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logger;
    }
}
