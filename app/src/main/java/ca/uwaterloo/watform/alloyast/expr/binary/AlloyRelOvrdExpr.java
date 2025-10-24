package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyRelOvrdExpr extends AlloyBinaryExpr {
    public AlloyRelOvrdExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.REL_OVERRIDE);
    }

    public AlloyRelOvrdExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.REL_OVERRIDE);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
