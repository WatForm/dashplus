package ca.uwaterloo.watform.utils;

import java.io.*;
import java.util.logging.Logger;

public class EvalLogger {
    private final Logger logger;
    private int depth = 0;

    public EvalLogger(Logger logger) {
        this.logger = logger;
    }

    public void enter(String msg) {
        log("--> " + msg);
        depth++;
    }

    public void exit(String msg) {
        depth--;
        log("<-- " + msg);
    }

    public void log(String msg) {
        logger.fine("  ".repeat(depth) + msg);
    }
}
