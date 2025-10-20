package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public class DashInit extends DashExpr {

    public DashInit(Pos p, AlloyExpr e) {
        super(p, e);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.initName, sb, indent);
    }
}
