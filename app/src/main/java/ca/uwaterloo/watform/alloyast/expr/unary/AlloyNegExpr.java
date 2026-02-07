package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNegExpr extends AlloyUnaryExpr {
    public AlloyNegExpr(Pos pos, AlloyExpr sub) {
        super(pos, sub, AlloyStrings.NOT_EXCL);
    }

    public AlloyNegExpr(AlloyExpr sub) {
        super(sub, AlloyStrings.NOT_EXCL);
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
        pCtx.appendChild(this, this.sub);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyNegExpr rebuild(AlloyExpr sub) {
        return new AlloyNegExpr(this.pos, sub);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.UNI_TEMP;
    }
}
