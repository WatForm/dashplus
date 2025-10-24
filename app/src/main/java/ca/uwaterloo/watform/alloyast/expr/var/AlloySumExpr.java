package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloySumExpr extends AlloyVarExpr {
    public AlloySumExpr(Pos pos) {
        super(pos, AlloyStrings.SUM);
    }

    public AlloySumExpr() {
        super(AlloyStrings.SUM);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
