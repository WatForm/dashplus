package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.utils.*;

public class AlloyInterfaceImplError extends ImplementationError {

    private AlloyInterfaceImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyInterfaceImplError(String msg) {
        super(msg);
    }

    public static AlloyInterfaceImplError solutionEvalErr(Pos pos, String msg) {
        return new AlloyInterfaceImplError(pos, msg);
    }
}
