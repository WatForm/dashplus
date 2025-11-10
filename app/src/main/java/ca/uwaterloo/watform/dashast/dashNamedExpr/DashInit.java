package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStateItem;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;

public final class DashInit extends DashNamedExpr implements DashStateItem {

    public DashInit(Pos p, AlloyExpr e) {
        super(p, e);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.initName, sb, indent);
    }
}
