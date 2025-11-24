package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.utils.*;

public class AlloyInterfaceError extends RuntimeException {
    public final Pos pos;

    private AlloyInterfaceError(Pos pos, String msg) {
        super(msg);
        this.pos = pos;
    }

    private AlloyInterfaceError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyInterfaceError solutionEvalErr(Pos pos, String msg) {
        return new AlloyInterfaceError(pos, msg);
    }
}
