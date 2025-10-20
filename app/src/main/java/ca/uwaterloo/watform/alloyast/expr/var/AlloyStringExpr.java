package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.utils.*;

public final class AlloyStringExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloyStringExpr(Pos pos) {
        super(pos, AlloyStrings.STRING);
    }

    public AlloyStringExpr() {
        super(AlloyStrings.STRING);
    }
}
