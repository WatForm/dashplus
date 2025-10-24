package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloySeqExpr extends AlloyNameExpr implements AlloyScopableExpr {
    public AlloySeqExpr(Pos pos) {
        super(pos, AlloyStrings.SEQ);
    }

    public AlloySeqExpr() {
        super(AlloyStrings.SEQ);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
