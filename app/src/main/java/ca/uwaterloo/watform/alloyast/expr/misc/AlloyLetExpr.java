package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public final class AlloyLetExpr extends AlloyExpr {
	public static final class AlloyLetAsn extends AlloyASTNode {
		public final AlloyNameExpr name;
		public final AlloyExpr expr;

		public AlloyLetAsn(Pos pos, AlloyNameExpr name, AlloyExpr expr) {
			super(pos);
			this.name = name;
			this.expr = expr;
		}

		public AlloyNameExpr getName() {
			return name;
		}

		public AlloyExpr getExpr() {
			return expr;
		}

		@Override
		public void toString(StringBuilder sb, int indent) {
			sb.append(name.toString());
			sb.append(AlloyStrings.EQUAL);
			expr.toString(sb, indent);
		}
	}

	public final List<AlloyLetAsn> asns;
	public final AlloyExpr body;

	public AlloyLetExpr(Pos pos, List<AlloyLetAsn> asns, AlloyExpr body) {
		super(pos);
		this.asns = Collections.unmodifiableList(asns);
		this.body = body;
	}

	public AlloyLetExpr(Pos pos, AlloyLetAsn asn, AlloyExpr body) {
		super(pos);
		this.asns = Collections.unmodifiableList(Collections.singletonList(asn));
		this.body = body;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(AlloyStrings.LET);
		sb.append(AlloyStrings.SPACE);
		ASTNode.join(sb, indent, this.asns, AlloyStrings.COMMA + AlloyStrings.SPACE);
		sb.append(AlloyStrings.SPACE);
		sb.append(AlloyStrings.BAR);
		sb.append(AlloyStrings.SPACE);
		this.body.toString(sb, indent);
	}
}
