package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

public final class AlloyStateSeqExpr extends AlloyBinaryExpr {
    public AlloyStateSeqExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.SEQUENCE_OP);
    }

    public AlloyStateSeqExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.SEQUENCE_OP);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyStateSeqExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyStateSeqExpr(this.pos, left, right);
    }
}
