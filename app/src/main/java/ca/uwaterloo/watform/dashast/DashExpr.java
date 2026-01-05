/*
	This is just a wrapper class in the class
	hierarchy for a DashExpr
*/

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;

public abstract class DashExpr extends AlloyExpr {
    public DashExpr(Pos p) {}
}
