package ca.uwaterloo.watform.utils;

// borrowed from Alloy
// NADTODO fix up pos and error messages
// NADTODO this is temporary

public class ErrorUser extends RuntimeException {

    public Pos pos;

    public ErrorUser(String msg) {
        super(msg);
    }

    public ErrorUser(Pos pos, String msg) {
        super(msg, null);
        this.pos = pos;
    }
}
