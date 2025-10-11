package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.AlloyStrings;

public final class AlloyPrimeExpr extends AlloyUnaryExpr {
	public AlloyPrimeExpr(Pos pos, AlloyExpr sub) {
		super(pos, sub, AlloyStrings.PRIME);
	}

	@Override
	public final String toString() {
		return sub.toString() + op;
	}
}
