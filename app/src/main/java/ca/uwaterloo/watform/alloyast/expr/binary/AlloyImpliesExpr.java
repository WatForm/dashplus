package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyImpliesExpr extends AlloyBinaryExpr {
    public AlloyImpliesExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.RFATARROW);
    }

    public AlloyImpliesExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.RFATARROW);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyImpliesExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyImpliesExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.IMPLIES_PREC;
    }

    @Override
    public boolean isLeftAssoc() {
        return false;
    }
}
