package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloySumExpr extends AlloyVarExpr {
	public AlloySumExpr(Pos pos) {
		super(pos, AlloyStrings.SUM);
	}
}
