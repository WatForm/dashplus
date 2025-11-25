package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.utils.*;

public class AlloyInterfaceError extends DashplusError {
    private AlloyInterfaceError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyInterfaceError(String msg) {
        super(msg);
    }

    public static AlloyInterfaceError solutionEvalErr(Pos pos, String msg) {
        return new AlloyInterfaceError(pos, msg);
    }
}
