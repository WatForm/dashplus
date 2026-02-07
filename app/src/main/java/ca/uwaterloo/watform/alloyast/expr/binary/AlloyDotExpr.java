package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyDotExpr extends AlloyBinaryExpr {
    public AlloyDotExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.DOT);
    }

    public AlloyDotExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.DOT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDotExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(this.pos, left, right);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.left.toString(sb, indent);
        sb.append(op);
        this.right.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(LPAREN);
        pCtx.appendChild(this, this.left, false);
        pCtx.append(op);
        pCtx.brkNoSpace();
        pCtx.appendChild(this, this.right, false);
        pCtx.append(RPAREN);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.DOT;
    }
}
