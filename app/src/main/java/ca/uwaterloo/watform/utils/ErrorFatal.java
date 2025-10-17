package ca.uwaterloo.watform.utils;

// borrowed from Alloy
//NADTODO fix up pos and error messages

public class ErrorFatal extends RuntimeException {

    public Pos pos;

    public ErrorFatal(String msg) {
        super(msg);
    }

    public ErrorFatal(Pos pos, String msg) {
        super(msg, null);
        this.pos = pos;
    }

}