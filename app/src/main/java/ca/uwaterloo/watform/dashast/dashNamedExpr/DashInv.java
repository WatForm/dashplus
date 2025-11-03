package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStateItem;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;

public final class DashInv extends DashNamedExpr implements DashStateItem {

    String name;

    public DashInv(Pos p, AlloyExpr inv) {
        super(p, inv);
    }

    public DashInv(Pos p, String n, AlloyExpr inv) {
        super(p, inv);
        // Looking at Dash.cup, we allow invariants with no name. - Jack
        // assert (n != "");
        // assert (n != null);
        this.name = n;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.invName, sb, indent);
    }
}
