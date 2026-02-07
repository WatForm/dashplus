package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

// Part of the comparison grammar rule in .g4, but made a distinct class b/c it's used frequently
public final class AlloyEqualsExpr extends AlloyBinaryExpr {
    public AlloyEqualsExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.EQUAL);
    }

    public AlloyEqualsExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.EQUAL);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyEqualsExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyEqualsExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.COMP;
    }
}
