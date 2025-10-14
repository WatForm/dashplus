package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyQuantificationExpr extends AlloyExpr {
	public final Quant quant;
	public final List<AlloyDecl> decls;
	public final AlloyExpr body;

	public AlloyQuantificationExpr(
			Pos pos, AlloyQuantificationExpr.Quant quant, List<AlloyDecl> decls, AlloyExpr body) {
		super(pos);
		this.quant = quant;
		this.decls = Collections.unmodifiableList(decls);
		this.body = body;
	}

	public enum Quant {
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

		private Quant(String label) {
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
	public void toString(StringBuilder sb, int indent) {
		sb.append(this.quant.toString());
		sb.append(AlloyStrings.SPACE);
		boolean first = true;
		for(AlloyDecl decl : this.decls) {
			if(! first) {
				sb.append(", ");
			}
			decl.toString(sb, indent);
			first = false;
		}
		sb.append(AlloyStrings.SPACE);
		sb.append(AlloyStrings.BAR);
		sb.append(AlloyStrings.SPACE);
		this.body.toString(sb, indent);
	}
}
