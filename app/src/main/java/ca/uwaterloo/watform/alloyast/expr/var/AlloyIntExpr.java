package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyIntExpr extends AlloyVarExpr {
    public AlloyIntExpr(Pos pos) {
        super(pos, AlloyStrings.INT);
    }

    public AlloyIntExpr() {
        super(AlloyStrings.INT);
    }
}
