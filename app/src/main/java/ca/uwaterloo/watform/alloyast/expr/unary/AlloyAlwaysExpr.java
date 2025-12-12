package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyAlwaysExpr extends AlloyUnaryExpr {
    public AlloyAlwaysExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.ALWAYS);
    }

    public AlloyAlwaysExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.ALWAYS);
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
    public AlloyAlwaysExpr rebuild(AlloyExpr sub) {
        return new AlloyAlwaysExpr(this.pos, sub);
    }
}
