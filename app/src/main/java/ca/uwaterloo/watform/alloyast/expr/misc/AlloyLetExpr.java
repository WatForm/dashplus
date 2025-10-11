package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.helper.*;
import ca.uwaterloo.watform.utils.AlloyStrings;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	public String toString() {
		return AlloyStrings.LET
				+ " "
				+ asns.stream().map(AlloyAsnExprHelper::toString).collect(Collectors.joining(", "))
				+ " "
				+ AlloyStrings.BAR
				+ " "
				+ body.toString()
				+ " ";
	}
}
