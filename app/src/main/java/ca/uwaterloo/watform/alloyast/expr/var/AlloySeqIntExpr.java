package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloySeqIntExpr extends AlloyVarExpr implements AlloySigRefExpr {
    public AlloySeqIntExpr(Pos pos) {
        super(pos, AlloyStrings.SEQ_INT);
    }

    public AlloySeqIntExpr() {
        super(AlloyStrings.SEQ_INT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloySeqIntExpr rebuild(String label) {
        return new AlloySeqIntExpr(this.pos);
    }
}
