package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNoneExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloyNoneExpr(Pos pos) {
        super(pos, AlloyStrings.NONE);
    }

    public AlloyNoneExpr() {
        super(AlloyStrings.NONE);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
