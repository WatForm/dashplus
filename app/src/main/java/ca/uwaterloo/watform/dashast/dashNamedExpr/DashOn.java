package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;

public class DashOn extends DashNamedExpr implements DashTransItem {

    public DashOn(Pos pos, AlloyExpr e) {
        super(pos, e);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.onName, sb, indent);
    }
}
