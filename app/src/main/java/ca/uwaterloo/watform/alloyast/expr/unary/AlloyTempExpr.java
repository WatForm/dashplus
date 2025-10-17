package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyTempExpr extends AlloyUnaryExpr {
	public static enum Temporal {
		ALWAYS(AlloyStrings.ALWAYS),
		EVENTUALLY(AlloyStrings.EVENTUALLY),
		AFTER(AlloyStrings.AFTER),
		HISTORICALLY(AlloyStrings.HISTORICALLY),
		ONCE(AlloyStrings.ONCE),
		BEFORE(AlloyStrings.BEFORE);

		private final String label;

		Temporal(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return this.label;
		}
	}

	public final Temporal temp;

	public AlloyTempExpr(Pos pos, Temporal temp, AlloyExpr sub) {
		super(pos, sub, temp.toString());
		this.temp = temp;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(op);
		sb.append(AlloyStrings.SPACE);
		this.sub.toString(sb, indent);
	}
}
