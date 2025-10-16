package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public final class AlloyUnivExpr extends AlloyVarExpr {
	public AlloyUnivExpr(Pos pos) {
		super(pos, AlloyStrings.UNIV);
	}
}
