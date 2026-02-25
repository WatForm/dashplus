package ca.uwaterloo.watform.utils;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*

Usage:

Create a logger with a unique ID that prints logs to a specific <filename>, if debug is true.
This creates a file called <filename>.log, which is ignored by git
If debug is false, the logger does not print or write any of the produced logs.
The filename can be reused with other loggers, each logger annotates the logs it writes with the part of the source code it is being used in

Logger l = CustomLoggerFactory.make("<filename>",debug);

l.info("test info message");
l.warning("test warning message");
l.fine("test fine message");
l.finer("test finer message");
l.config("test config message");

further documentation: https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html

A logger does not have to be threaded through multiple files.  If multiple loggers have the same
filename, they will write to the same file.

*/

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
