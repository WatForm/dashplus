package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNumExpr extends AlloyVarExpr {
	public final boolean isPositive;
	public final int value;

	public AlloyNumExpr(Pos pos, boolean isNegative, String numLabel) {
		super(pos, numLabel);
		this.value = Integer.parseInt(numLabel);
		this.isPositive= isNegative;
	}
}
