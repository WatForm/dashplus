package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Objects;

public final class AlloyParenExpr extends AlloyExpr {
    public final AlloyExpr sub;

    public AlloyParenExpr(Pos pos, AlloyExpr sub) {
        super(pos);
        this.sub = sub;
        reqNonNull(nullField(pos, this), this.sub);
    }

    public AlloyParenExpr(AlloyExpr sub) {
        this(Pos.UNKNOWN, sub);
    }

    public AlloyExpr getSub() {
        return sub;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(AlloyStrings.LPAREN);
        this.sub.toString(sb, indent);
        sb.append(AlloyStrings.RPAREN);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(LPAREN);
        pCtx.brkNoSpace();
        this.sub.ppNewBlock(pCtx);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RPAREN);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sub);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyParenExpr other = (AlloyParenExpr) obj;
        if (sub == null) {
            if (other.sub != null) return false;
        } else if (!sub.equals(other.sub)) return false;
        return true;
    }
}
