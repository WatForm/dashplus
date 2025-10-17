package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

import ca.uwaterloo.watform.dashast.DashStrings;

public class DashExited extends DashExpr {

    public DashExited(Pos p, AlloyExpr e) {
        super(p,e);
    }
 
    public String toString(Integer indent) {
        return super.toString(DashStrings.exitName, indent);
    }
}
