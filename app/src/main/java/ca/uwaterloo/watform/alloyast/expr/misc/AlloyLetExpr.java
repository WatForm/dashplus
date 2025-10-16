package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.helper.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import java.util.Collections;
import java.util.List;

public final class AlloyLetExpr extends AlloyExpr {
	public final List<AlloyAsnExprHelper> asns;
	public final AlloyExpr body;

	public AlloyLetExpr(Pos pos, List<AlloyAsnExprHelper> asns, AlloyExpr body) {
		super(pos);
		this.asns = Collections.unmodifiableList(asns);
		this.body = body;
	}

	public AlloyLetExpr(Pos pos, AlloyAsnExprHelper asn, AlloyExpr body) {
		super(pos);
		this.asns = Collections.unmodifiableList(Collections.singletonList(asn));
		this.body = body;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(AlloyStrings.LET);
		sb.append(AlloyStrings.SPACE);
		boolean first = true;
		for (AlloyAsnExprHelper asn : asns) {
			if (!first) {
				sb.append(", ");
			}
			asn.toString(sb, indent);
			first = false;
		}
		sb.append(AlloyStrings.SPACE);
		sb.append(AlloyStrings.BAR);
		sb.append(AlloyStrings.SPACE);
		this.body.toString(sb, indent);
	}
}
