package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public class DashFrom extends DashExpr {

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.fromName, sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
