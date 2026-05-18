package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;

public class DashASTError extends UserOrImplError {
    public final Pos pos;

    private DashASTError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    private DashASTError(Pos pos, String msg) {
        super(msg);
        this.pos = pos;
    }

    // ====================================================================================
    // DashFile
    // ====================================================================================
    public static DashASTError exactlyOneStateRoot() {
        return new DashASTError("A dash file should contain exactly one root state. ");
    }

    public static DashASTError invalidAlloyQtEnum(Pos pos, String msg) {
        return new DashASTError(pos, msg);
    }
}
