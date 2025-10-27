package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyThisExpr extends AlloyVarExpr {
    public AlloyThisExpr(Pos pos) {
        super(pos, AlloyStrings.THIS);
    }

    public AlloyThisExpr() {
        super(AlloyStrings.THIS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
