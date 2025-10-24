package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFunMaxExpr extends AlloyVarExpr {
    public AlloyFunMaxExpr(Pos pos) {
        super(pos, AlloyStrings.FUNMAX);
    }

    public AlloyFunMaxExpr() {
        super(AlloyStrings.FUNMAX);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
