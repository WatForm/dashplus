package ca.uwaterloo.watform.utils;

public abstract class DiagnosticException extends RuntimeException {
    public final Pos pos;

    public DiagnosticException(Pos pos, String message) {
        super(message);
        this.pos = pos;
    }

    public DiagnosticException(String message) {
        this(Pos.UNKNOWN, message);
    }
}
