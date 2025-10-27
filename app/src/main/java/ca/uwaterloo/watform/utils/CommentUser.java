package ca.uwaterloo.watform.utils;

// borrowed from Alloy
// NADTODO fix up pos and error messages
// NADTODO this is temporary

public class CommentUser extends DiagnosticException {
    public CommentUser(Pos pos, String msg) {
        super(pos, msg);
    }

    public CommentUser(String msg) {
        super(msg);
    }
}
