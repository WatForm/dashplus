package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyBeforeExpr extends AlloyUnaryExpr {
    public AlloyBeforeExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.BEFORE);
    }

    public AlloyBeforeExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.BEFORE);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(op);
        sb.append(AlloyStrings.SPACE);
        this.sub.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(op + SPACE);
        this.sub.pp(pCtx);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyBeforeExpr rebuild(AlloyExpr sub) {
        return new AlloyBeforeExpr(this.pos, sub);
    }
}
