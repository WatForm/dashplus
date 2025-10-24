package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyDisjExpr extends AlloyVarExpr {
    public AlloyDisjExpr(Pos pos) {
        super(pos, AlloyStrings.DISJ);
    }

    public AlloyDisjExpr() {
        super(AlloyStrings.DISJ);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
