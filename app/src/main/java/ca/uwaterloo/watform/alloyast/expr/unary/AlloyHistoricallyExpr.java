package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
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
    public void pp(PrintContext pCtx) {
        pCtx.append(op + SPACE);
        pCtx.appendChild(this, this.sub);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyHistoricallyExpr rebuild(AlloyExpr sub) {
        return new AlloyHistoricallyExpr(this.pos, sub);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.UNI_TEMP;
    }
}
