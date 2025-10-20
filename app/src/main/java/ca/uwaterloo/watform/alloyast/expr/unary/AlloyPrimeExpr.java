package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyPrimeExpr extends AlloyUnaryExpr {
    public AlloyPrimeExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.PRIME);
    }

    @Override
    public final void toString(StringBuilder sb, int indent) {
        this.sub.toString(sb, indent);
        sb.append(this.op);
    }
}
