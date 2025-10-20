package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyQtExpr extends AlloyUnaryExpr {
    public enum Quant {
        ALL(AlloyStrings.ALL),
        NO(AlloyStrings.NO),
        SOME(AlloyStrings.SOME),
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SET(AlloyStrings.SET),
        SEQ(AlloyStrings.SEQ);

        public final String label;

        private Quant(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public final String toString() {
            return label;
        }
    }

    public final Quant qt;

    public AlloyQtExpr(Pos pos, Quant qt, AlloyExpr sub) {
        super(pos, sub, qt.toString());
        this.qt = qt;
    }

    public AlloyQtExpr(Quant qt, AlloyExpr sub) {
        super(sub, qt.toString());
        this.qt = qt;
    }

    @Override
    public final void toString(StringBuilder sb, int indent) {
        sb.append(this.qt);
        sb.append(AlloyStrings.SPACE);
        this.sub.toString(sb, indent);
    }
}
