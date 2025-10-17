package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class DashInit extends DashExpr {	

    public DashInit(Pos p, AlloyExpr e) {
        super(p,e);
    }
    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.initName, sb, indent);
    }
}
