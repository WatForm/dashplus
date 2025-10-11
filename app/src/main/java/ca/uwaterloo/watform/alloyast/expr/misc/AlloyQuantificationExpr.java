package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyQuantificationExpr extends AlloyExpr {
	public final Op op;
	public final List<AlloyDecl> decls;
	public final AlloyExpr body;

	public AlloyQuantificationExpr(
			Pos pos, AlloyQuantificationExpr.Op op, List<AlloyDecl> decls, AlloyExpr body) {
		super(pos);
		this.op = op;
		this.decls = Collections.unmodifiableList(decls);
		this.body = body;
	}

	public enum Op {
		/** all a,b:x | formula */
		ALL(AlloyStrings.ALL),
		/** no a,b:x | formula */
		NO(AlloyStrings.NO),
		/** lone a,b:x | formula */
		LONE(AlloyStrings.LONE),
		/** one a,b:x | formula */
		ONE(AlloyStrings.ONE),
		/** some a,b:x | formula */
		SOME(AlloyStrings.SOME),
		/** sum a,b:x | intExpression */
		SUM(AlloyStrings.SUM);
		// Alloy has Comprehension here too, but that's made into a separate
		// class here

		public final String label;

		private Op(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		/** Returns the human readable label for this operator */
		@Override
		public final String toString() {
			return label;
		}
	}

	@Override
	public String toString() {
		return op.toString()
				+ " "
				+ decls.stream().map(AlloyDecl::toString).collect(Collectors.joining(", "))
				+ " "
				+ AlloyStrings.BAR
				+ " "
				+ body.toString()
				+ " ";
	}
}
