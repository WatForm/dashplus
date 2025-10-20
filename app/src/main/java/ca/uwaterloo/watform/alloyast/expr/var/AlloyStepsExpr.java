package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.utils.*;

public final class AlloyStepsExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloyStepsExpr(Pos pos) {
        super(pos, AlloyStrings.STEPS);
    }

    public AlloyStepsExpr() {
        super(AlloyStrings.STEPS);
    }
}
