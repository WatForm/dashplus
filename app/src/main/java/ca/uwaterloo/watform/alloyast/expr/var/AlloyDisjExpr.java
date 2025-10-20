package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyDisjExpr extends AlloyVarExpr {
    public AlloyDisjExpr(Pos pos) {
        super(pos, AlloyStrings.DISJ);
    }
}
