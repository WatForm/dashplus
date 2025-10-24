package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyReflTransClosExpr extends AlloyUnaryExpr {
    public AlloyReflTransClosExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.REFL_TRANS_CLOS);
    }

    public AlloyReflTransClosExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.REFL_TRANS_CLOS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
