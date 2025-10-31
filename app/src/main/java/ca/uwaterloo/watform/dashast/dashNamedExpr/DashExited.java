package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public final class DashExited extends DashNamedExpr implements DashStateItem {
    public DashExited(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.exitName, sb, indent);
    }
}
