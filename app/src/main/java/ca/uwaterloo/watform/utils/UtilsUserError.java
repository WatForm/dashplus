package ca.uwaterloo.watform.utils;

public class UtilsUserError extends UserOrImplError {

    private UtilsUserError(String msg) {
        super(msg);
    }

    private UtilsUserError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static UtilsUserError fileNotFound(String filePath, String causeMsg) {
        return new UtilsUserError("File not found or unreadable: " + filePath + ": " + causeMsg);
    }
}
