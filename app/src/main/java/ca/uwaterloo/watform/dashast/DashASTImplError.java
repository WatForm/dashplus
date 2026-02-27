package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Pos;

public final class DashASTImplError extends ImplementationError {
    private DashASTImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    private DashASTImplError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. So it cannot occur during parsing;
     * it must be an ImplementationError
     *
     * @param pos
     * @param field1
     * @param field2
     * @param className
     * @return AlloyASTImplError
     */
    public static DashASTImplError missingMul(Pos pos, AlloyExpr expr) {
        return new DashASTImplError(pos, expr.toString() + "must be given a multiplicity.");
    }
}
