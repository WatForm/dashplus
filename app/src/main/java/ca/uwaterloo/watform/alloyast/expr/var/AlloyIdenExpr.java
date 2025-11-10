package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyIdenExpr extends AlloyVarExpr {
    public AlloyIdenExpr(Pos pos) {
        super(pos, AlloyStrings.IDEN);
    }

    public AlloyIdenExpr() {
        super(AlloyStrings.IDEN);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyIdenExpr rebuild(String label) {
        return new AlloyIdenExpr(this.pos);
    }
}
