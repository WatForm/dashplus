package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import java.util.ArrayList;
import java.util.List;

public final class AlloyQuantificationExpr extends AlloyExpr {
	public List<AlloyDecl> declList = new ArrayList<>();

	public AlloyQuantificationExpr(Pos pos, AlloyQuantificationExpr.Op op) {
		super(pos);
	}

	public enum Op {
		/** all a,b:x | formula */
		ALL("all"),
		/** no a,b:x | formula */
		NO("no"),
		/** lone a,b:x | formula */
		LONE("lone"),
		/** one a,b:x | formula */
		ONE("one"),
		/** some a,b:x | formula */
		SOME("some"),
		/** sum a,b:x | intExpression */
		SUM("sum");
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
}
