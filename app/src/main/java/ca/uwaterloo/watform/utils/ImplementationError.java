package ca.uwaterloo.watform.utils;

// borrowed from Alloy
// NADTODO fix up pos and error messages

public class ImplementationError extends DashplusError {
    public ImplementationError(String msg) {
        super(msg);
    }

    public ImplementationError(Pos pos, String msg) {
        super(pos, msg);
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
        return new ImplementationError(pos, methodName + " should not be called. ");
    }

    @Override
    public String toString() {
        String s = "";
        s += this.toStringMsg();
        s += this.toStringPosList();
        return s;
    }
}
