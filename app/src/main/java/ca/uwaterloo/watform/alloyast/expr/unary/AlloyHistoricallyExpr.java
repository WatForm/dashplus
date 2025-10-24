package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyHistoricallyExpr extends AlloyUnaryExpr {
    public AlloyHistoricallyExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.HISTORICALLY);
    }

    public AlloyHistoricallyExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.HISTORICALLY);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(op);
        sb.append(AlloyStrings.SPACE);
        this.sub.toString(sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
