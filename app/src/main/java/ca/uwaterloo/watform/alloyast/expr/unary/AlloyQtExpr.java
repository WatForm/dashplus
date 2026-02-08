package ca.uwaterloo.watform.alloyast.expr.unary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyQtExpr extends AlloyUnaryExpr {
    public enum Quant {
        ALL(AlloyStrings.ALL),
        NO(AlloyStrings.NO),
        SOME(AlloyStrings.SOME),
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SET(AlloyStrings.SET),
        SEQ(AlloyStrings.SEQ);

        public final String label;

        private Quant(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public final String toString() {
            return label;
        }
    }

    public final Quant qt;

    public AlloyQtExpr(Pos pos, Quant qt, AlloyExpr sub) {
        super(pos, sub, qt.toString());
        this.qt = qt;
        reqNonNull(nullField(pos, this), this.qt);
    }

    public AlloyQtExpr(Quant qt, AlloyExpr sub) {
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
