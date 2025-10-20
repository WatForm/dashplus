package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNegExpr extends AlloyUnaryExpr {
    public static enum Negation {
        NOT_EXCL(AlloyStrings.NOT_EXCL),
        NOT(AlloyStrings.NOT);

        private final String label;

        Negation(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public final Negation neg;

    public AlloyNegExpr(Pos pos, Negation neg, AlloyExpr sub) {
        super(pos, sub, neg.toString());
        this.neg = neg;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(op);
        sb.append(AlloyStrings.SPACE);
        this.sub.toString(sb, indent);
    }
}
