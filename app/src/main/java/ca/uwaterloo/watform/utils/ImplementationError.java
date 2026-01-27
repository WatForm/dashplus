package ca.uwaterloo.watform.utils;

// borrowed from Alloy
// NADTODO fix up pos and error messages

public class ImplementationError extends DashPlusException {
    public ImplementationError(String msg) {
        super(msg);
    }

    public ImplementationError(Pos pos, String msg) {
        super(pos, msg);
    }

    public ImplementationError(DashPlusException other) {
        super(other.posList, other.getMessage());
    }

    // missing cases in the code
    public static ImplementationError missingCase(String x) throws ImplementationError {
        return new ImplementationError("missing case " + x);
    }

    // failed dynamic cast; incorrect assumption about object's runtime type
    public static ImplementationError failedCast(String s) throws ImplementationError {
        return new ImplementationError("Failed Dynamic Cast: " + s);
    }

    public static ImplementationError methodShouldNotBeCalled(String methodName)
            throws ImplementationError {
        return new ImplementationError(methodName + " should not be called");
    }

    public static ImplementationError methodShouldNotBeCalled(Pos pos, String methodName)
            throws ImplementationError {
        return new ImplementationError(pos, methodName + " should not be called. ");
    }

    public static ImplementationError shouldNotReach() throws ImplementationError {
        return new ImplementationError("should not be called reach this point");
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. Since it cannot occur during
     * parsing; it must be an ImplementationError
     *
     * @param pos
     * @param object
     * @return
     */
    public static ImplementationError nullField(Pos pos, Object object) {
        return new ImplementationError(
                pos, object.getClass().getSimpleName() + " cannot have null or blank fields. ");
    }
}
