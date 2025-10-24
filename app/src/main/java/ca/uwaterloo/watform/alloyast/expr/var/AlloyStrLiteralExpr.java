package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyStrLiteralExpr extends AlloyVarExpr {
    public AlloyStrLiteralExpr(Pos pos, String strLiteral) {
        super(pos, strLiteral);
    }

    public AlloyStrLiteralExpr(String strLiteral) {
        super(strLiteral);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
