package ca.uwaterloo.watform.utils;

public class UtilsImplError extends ImplementationError {

    private UtilsImplError(String msg) {
        super(msg);
    }

    private UtilsImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static UtilsImplError xmlConfigError(String context, String causeMsg) {
        return new UtilsImplError("XML configuration error while " + context + ": " + causeMsg);
    }

    public static UtilsImplError xmlWriteError(String context, String causeMsg) {
        return new UtilsImplError("Failed to write XML for " + context + ": " + causeMsg);
    }
}
