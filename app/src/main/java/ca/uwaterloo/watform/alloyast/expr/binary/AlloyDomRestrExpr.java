package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyDomRestrExpr extends AlloyBinaryExpr {
    public AlloyDomRestrExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.DOMRESTR);
    }

    public AlloyDomRestrExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.DOMRESTR);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDomRestrExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyDomRestrExpr(this.pos, left, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.DOM_RESTR_PREC;
    }
}
