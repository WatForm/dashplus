package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// another way of writing a dot join
public final class AlloyBracketExpr extends AlloyExpr {
    public final AlloyExpr expr;
    public final List<AlloyExpr> exprs;

    public AlloyBracketExpr(Pos pos, AlloyExpr expr, List<AlloyExpr> exprs) {
        super(pos);
        this.expr = expr;
        this.exprs = Collections.unmodifiableList(exprs);
        reqNonNull(nullField(pos, this), this.expr, this.exprs);
    }

    public AlloyBracketExpr(AlloyExpr expr, List<AlloyExpr> exprs) {
        this(Pos.UNKNOWN, expr, exprs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.expr.toString(sb, indent);
        sb.append(AlloyStrings.LBRACK);
        ASTNode.join(sb, indent, exprs, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.RBRACK);
    }

    @Override
    public void pp(PrintContext pCtx) {
        this.expr.pp(pCtx);
        pCtx.append(LBRACK);
        pCtx.brkNoSpace();
        pCtx.appendList(this.exprs, COMMA);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RBRACK);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.expr, this.exprs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyBracketExpr other = (AlloyBracketExpr) obj;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        if (exprs == null) {
            if (other.exprs != null) return false;
        } else if (!exprs.equals(other.exprs)) return false;
        return true;
    }

    @Override
    public int getPrec() {
        return AlloyExpr.BRACKET;
    }
}
