package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.AlloyStrings;

public final class AlloyStringExpr extends AlloyVarExpr {
	public AlloyStringExpr(Pos pos) {
		super(pos, AlloyStrings.STRING);
	}
}
