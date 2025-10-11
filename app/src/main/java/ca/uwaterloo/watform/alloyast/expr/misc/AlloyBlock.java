package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.AlloyStrings;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyBlock extends AlloyExpr {
	public final List<AlloyExpr> exprs;

	public AlloyBlock(Pos pos, List<AlloyExpr> exprs) {
		super(pos);
		this.exprs = Collections.unmodifiableList(exprs);
	}

	public AlloyBlock(Pos pos, AlloyExpr expr) {
		super(pos);
		this.exprs = Collections.unmodifiableList(Collections.singletonList(expr));
	}

	@Override
	public String toString() {
		return AlloyStrings.LBRACE
				+ "\n"
				+ exprs.stream().map(AlloyExpr::toString).collect(Collectors.joining("\n"))
				+ "\n"
				+ AlloyStrings.RBRACE
				+ "\n";
	}
}
