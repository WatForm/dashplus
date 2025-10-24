package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public class AlloyNameExpr extends AlloyVarExpr {
    public AlloyNameExpr(Pos pos, String label) {
        super(pos, label);
    }

    public AlloyNameExpr(String label) {
        super(label);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
