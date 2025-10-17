package ca.uwaterloo.watform.dashast;


import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

import ca.uwaterloo.watform.dashast.DashStrings;

public class DashEntered extends DashExpr {

    public DashEntered(Pos p, AlloyExpr e) {
        super(p,e);
    }
    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.enterName, sb, indent);
    }
}
