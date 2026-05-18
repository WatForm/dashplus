package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;

public final class DashASTImplError extends UserOrImplError {

    private DashASTImplError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    private DashASTImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    // nothing that needs a posList

    /*
    public static DashASTImplError missingMul(Pos pos, AlloyExpr expr) {
        return new DashASTImplError(pos, expr.toString() + "must be given a multiplicity.");
    }
    */

    public static DashASTImplError ancesNotPrefix(String s) {
        return new DashASTImplError("ances must be a prefix of dest: (ances,dest)" + s);
    }

    public static DashASTImplError chopPrefixFromFQNwithNoPrefix(String s) {
        return new DashASTImplError("chopPrefixFromFQNwithNoPrefix: " + s);
    }
}
