package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyQtExpr extends AlloyUnaryExpr {
    public final AlloyQtEnum qt;

    public AlloyQtExpr(Pos pos, AlloyQtEnum qt, AlloyExpr sub) {
        super(pos, sub, qt.toString());
        this.qt = qt;
        reqNonNull(nullField(pos, this), this.qt);
        if (this.qt == AlloyQtEnum.EXACTLY) {
            throw AlloyASTImplError.invalidAlloyQtEnum(
                    pos, this.getClass().getSimpleName() + ".qt cannot be AlloyQtEnum.EXACTLY. ");
        }
    }

    public AlloyQtExpr(AlloyQtEnum qt, AlloyExpr sub) {
        this(Pos.UNKNOWN, qt, sub);
    }

    @Override
    public final void toString(StringBuilder sb, int indent) {
        sb.append(this.qt);
        sb.append(AlloyStrings.SPACE);
        this.sub.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(this.qt.toString() + SPACE);
        pCtx.appendChild(this, this.sub, true);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyQtExpr rebuild(AlloyExpr sub) {
        return new AlloyQtExpr(this.pos, this.qt, sub);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.QUANTIFIED_PREC;
    }
}
