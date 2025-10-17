package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyUnivExpr extends AlloyVarExpr implements AlloySigRefExpr {
	public AlloyUnivExpr(Pos pos) {
		super(pos, AlloyStrings.UNIV);
	}
}
