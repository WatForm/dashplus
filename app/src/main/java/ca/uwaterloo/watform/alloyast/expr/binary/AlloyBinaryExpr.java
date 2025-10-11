package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;

public abstract class AlloyBinaryExpr extends AlloyExpr {
	public final AlloyExpr left;
	public final AlloyExpr right;
	public final String op;

	public AlloyBinaryExpr(Pos pos, AlloyExpr left, AlloyExpr right, String op) {
		super(pos);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public AlloyExpr getLeft() {
		return left;
	}

	public AlloyExpr getRight() {
		return right;
	}

	public String getOp() {
		return op;
	}

	@Override
	public String toString() {
		return left.toString() + op + right.toString();
	}
}
