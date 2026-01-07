package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyPrimeExpr extends AlloyUnaryExpr {
    public AlloyPrimeExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.PRIME);
    }

    public AlloyPrimeExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.PRIME);
    }

    @Override
    public final void toString(StringBuilder sb, int indent) {
        this.sub.toString(sb, indent);
        sb.append(this.op);
    }

    @Override
    public void pp(PrintContext pCtx) {
        this.sub.pp(pCtx);
        pCtx.append(this.op);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyPrimeExpr rebuild(AlloyExpr sub) {
        return new AlloyPrimeExpr(this.pos, sub);
    }
}
