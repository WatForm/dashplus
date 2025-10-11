package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;

public abstract class AlloyBinaryExpr extends AlloyExpr {
	public final AlloyExpr left;
	public final AlloyExpr right;

	public AlloyBinaryExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
		super(pos);
		this.left = left;
		this.right = right;
	}

	public AlloyExpr getLeft() {
		return left;
	}

	public AlloyExpr getRight() {
		return right;
	}
}
