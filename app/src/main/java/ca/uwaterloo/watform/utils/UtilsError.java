package ca.uwaterloo.watform.utils;

public class UtilsError extends UserOrImplError {

    private UtilsError(String msg) {
        super(msg);
    }

    private UtilsError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static UtilsError malformedXml(String xmlPath, String causeMsg) {
        return new UtilsError("Could not parse XML as well-formed: " + xmlPath + ": " + causeMsg);
    }
}
