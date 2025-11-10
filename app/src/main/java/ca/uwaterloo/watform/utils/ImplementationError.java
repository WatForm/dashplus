package ca.uwaterloo.watform.utils;

// borrowed from Alloy
// NADTODO fix up pos and error messages

public class ImplementationError extends RuntimeException {
    public Pos pos;

    public ImplementationError(String msg) {
        super(msg);
    }

    public ImplementationError(Pos pos, String msg) {
        super(msg, null);
        this.pos = pos;
    }

    // missing cases in the code
    public static ImplementationError missingCase(String x) throws ImplementationError {
        return new ImplementationError("missing case " + x);
    }

    // failed dynamic cast; incorrect assumption about object's runtime type
    public static ImplementationError failedCast(String s) {
        return new ImplementationError("Failed Dynamic Cast: " + s);
    }

    public static ImplementationError methodShouldNotBeCalled() {
        return new ImplementationError("This method should not be called");
    }

    public static ImplementationError methodShouldNotBeCalled(Pos pos, String methodName) {
        return new ImplementationError(
                pos, methodName + " should not be called: " + pos.toString());
    }
}
