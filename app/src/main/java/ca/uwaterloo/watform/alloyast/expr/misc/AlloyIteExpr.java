package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Objects;

public final class AlloyIteExpr extends AlloyExpr {
    public final AlloyExpr cond;
    public final AlloyExpr conseq;
    public final AlloyExpr alt;

    public AlloyIteExpr(Pos pos, AlloyExpr cond, AlloyExpr conseq, AlloyExpr alt) {
        super(pos);
        this.cond = cond;
        this.conseq = conseq;
        this.alt = alt;
        reqNonNull(nullField(pos, this), this.cond, this.conseq, this.alt);
    }

    public AlloyIteExpr(AlloyExpr cond, AlloyExpr conseq, AlloyExpr alt) {
        this(Pos.UNKNOWN, cond, conseq, alt);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.cond.toString(sb, indent);
        sb.append(SPACE);
        sb.append(RFATARROW);
        sb.append(SPACE);
        this.conseq.toString(sb, indent);
        sb.append(SPACE);
        sb.append(ELSE);
        sb.append(SPACE);
        this.alt.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(LPAREN);
        this.cond.pp(pCtx);
        pCtx.append(RPAREN);
        pCtx.brk();

        pCtx.begin();
        pCtx.append(RFATARROW + SPACE);
        pCtx.append(LPAREN);
        this.conseq.pp(pCtx);
        pCtx.append(RPAREN);
        pCtx.end();
        pCtx.brk();

        pCtx.begin();
        pCtx.append(ELSE + SPACE);
        pCtx.append(LPAREN);
        this.alt.pp(pCtx);
        pCtx.append(RPAREN);
        pCtx.end();
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cond, this.conseq, this.alt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyIteExpr other = (AlloyIteExpr) obj;
        if (cond == null) {
            if (other.cond != null) return false;
        } else if (!cond.equals(other.cond)) return false;
        if (conseq == null) {
            if (other.conseq != null) return false;
        } else if (!conseq.equals(other.conseq)) return false;
        if (alt == null) {
            if (other.alt != null) return false;
        } else if (!alt.equals(other.alt)) return false;
        return true;
    }
}
