package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import java.util.Collections;
import java.util.List;

// another way of writing a dot join
public final class AlloyBracketExpr extends AlloyExpr {
	public final AlloyExpr expr;
	public final List<AlloyExpr> exprs;

	public AlloyBracketExpr(Pos pos, AlloyExpr expr, List<AlloyExpr> exprs) {
		super(pos);
		this.expr = expr;
		this.exprs = Collections.unmodifiableList(exprs);
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		this.expr.toString(sb, indent);
		sb.append(AlloyStrings.LBRACK);
		ASTNode.join(sb, indent, exprs, AlloyStrings.COMMA + AlloyStrings.SPACE);
		sb.append(AlloyStrings.RBRACK);
	}
}
