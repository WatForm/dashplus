package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyAfterExpr extends AlloyUnaryExpr {
    public AlloyAfterExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.AFTER);
    }

    public AlloyAfterExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.AFTER);
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

    @Override
    public AlloyAfterExpr rebuild(AlloyExpr sub) {
        return new AlloyAfterExpr(this.pos, sub);
    }
}
