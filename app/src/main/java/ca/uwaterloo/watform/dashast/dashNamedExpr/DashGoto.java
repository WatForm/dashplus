package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;

public class DashGoto extends DashNamedExpr {

    public DashGoto(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.gotoName, sb, indent);
    }
}
