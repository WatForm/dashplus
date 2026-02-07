package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyIntersExpr extends AlloyBinaryExpr {
    public AlloyIntersExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.INTERSECTION);
    }

    public AlloyIntersExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.INTERSECTION);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyIntersExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyIntersExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.INTERSECT;
    }
}
