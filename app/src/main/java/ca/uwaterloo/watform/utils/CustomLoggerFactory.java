package ca.uwaterloo.watform.utils;

import java.io.*;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*

Usage:

Create a logger with a unique ID that prints logs to a specific <filename>, if debug is true.
This creates a file called <filename>.log, which is ignored by git
If debug is false, the logger does not print or write any of the produced logs.
The filename can be reused with other loggers, each logger annotates the logs it writes with the part of the source code it is being used in

Logger l = CustomLoggerFactory.make("<filename>.log",debug);

l.info("test info message");
l.warning("test warning message");
l.fine("test fine message");
l.finer("test finer message");
l.config("test config message");

further documentation: https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html

A logger does not have to be threaded through multiple files.  If multiple loggers have the same
filename, they will write to the same file, since they will share the same fileHandler

Having multiple fileHandlers share the same file results in strange behavior, since a handler, when unable to acquire a lock on the file (which may be held by another handler), the logger writes to <filename>.log.1, <filename>.log.2 etc. This behavior is ideally never seen, but as a failsafe, this pattern is included in the .gitignore

*/

public class CustomLoggerFactory {

    private static int UID = 0;

    private static HashMap<String, FileHandler> handlerTable = new HashMap<>();

    public static Logger make(String fileName, boolean debug) {
        Logger logger = Logger.getLogger("" + UID);
        UID += 1;

        fileName = fileName + ".log";

        logger.setUseParentHandlers(false); // remove ability to access console

        if (!debug) return logger;

        if (!handlerTable.containsKey(fileName)) makeNewFileAndHandler(fileName);

        logger.addHandler(handlerTable.get(fileName));

        return logger;
    }

    private static void makeNewFileAndHandler(String fileName) {
        File file = new File(fileName);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try {
            FileHandler fh = new FileHandler(fileName);
            fh.setFormatter(new SimpleFormatter());
            handlerTable.put(fileName, fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
