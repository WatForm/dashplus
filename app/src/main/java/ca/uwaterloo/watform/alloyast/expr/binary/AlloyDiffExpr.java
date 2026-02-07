package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyDiffExpr extends AlloyBinaryExpr {
    public AlloyDiffExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.MINUS);
    }

    public AlloyDiffExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.MINUS);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDiffExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyDiffExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.PLUS_MINUS_PREC;
    }
}
