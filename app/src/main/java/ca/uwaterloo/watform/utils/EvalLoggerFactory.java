package ca.uwaterloo.watform.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class EvalLoggerFactory {
    private static final Map<String, EvalLogger> loggerTable = new HashMap<>();

    // Class not meant to be instantiated
    private EvalLoggerFactory() {}

    // Allocates one logger per fileName
    public static EvalLogger make(String fileName, boolean debug) {
        if (loggerTable.containsKey(fileName)) {
            return loggerTable.get(fileName);
        } else {
            var logger =
                    new EvalLogger(
                            CustomLoggerFactory.make(
                                    fileName, debug, Level.ALL, new PlainFormatter()));
            loggerTable.put(fileName, logger);
            return logger;
        }
    }
}
