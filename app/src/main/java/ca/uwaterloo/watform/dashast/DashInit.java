package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.alloyast.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class DashInit extends DashExpr {	

    public DashInit(Pos p, AlloyExpr e) {
        super(p,e);
    }
    public String toString(Integer i) {
        return super.toString(DashStrings.initName, i);
    }
}
