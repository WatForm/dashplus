package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;

// send P[x]/ev1
// send ev1
// send y.x.ev1

public class DashSend extends DashNamedExpr {

    public DashSend(Pos pos, AlloyExpr e) {
        super(pos, e);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.sendName, sb, indent);
    }
}
