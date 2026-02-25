package ca.uwaterloo.watform.utils;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomFileLogger {

    public final Logger logger;
    private static int UID = 0;

    public CustomFileLogger(String fileName) {
        this.logger = Logger.getLogger("" + UID);
        UID += 1;

        fileName = fileName + ".log";

        this.logger.setUseParentHandlers(false); // remove ability to access console

        File file = new File(fileName);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try {
            FileHandler fh = new FileHandler(fileName);
            fh.setFormatter(new SimpleFormatter());
            this.logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
