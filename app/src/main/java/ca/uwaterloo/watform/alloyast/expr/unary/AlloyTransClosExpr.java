package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyTransClosExpr extends AlloyUnaryExpr {
    public AlloyTransClosExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.TRANS_CLOS);
    }

    public AlloyTransClosExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.TRANS_CLOS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyTransClosExpr rebuild(AlloyExpr sub) {
        return new AlloyTransClosExpr(this.pos, sub);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.TRANS_PREC;
    }
}
