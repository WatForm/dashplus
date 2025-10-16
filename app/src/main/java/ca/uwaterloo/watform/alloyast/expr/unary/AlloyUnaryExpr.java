package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public abstract class AlloyUnaryExpr extends AlloyExpr  {
	public final AlloyExpr sub;
	public final String op;

	public AlloyUnaryExpr(Pos pos, AlloyExpr sub, String op) {
		super(pos);
		this.sub = sub;
		this.op = op;
	}

	public AlloyExpr getSub() {
		return sub;
	}

	public String getOp() {
		return op;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(op);
		this.sub.toString(sb, indent);
	}
}
