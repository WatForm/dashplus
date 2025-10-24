package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public class DashGoto extends DashExpr {

    public DashGoto(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.gotoName, sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
