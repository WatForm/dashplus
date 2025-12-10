package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
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
    }

    public AlloyBracketExpr(AlloyExpr expr, List<AlloyExpr> exprs) {
        super();
        this.expr = expr;
        this.exprs = Collections.unmodifiableList(exprs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.expr.toString(sb, indent);
        sb.append(AlloyStrings.LBRACK);
        ASTNode.join(sb, indent, exprs, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.RBRACK);
    }

    @Override
    public void pp(PPrinter pp) {
        this.expr.pp(pp);
        pp.append(LBRACK);
        pp.brkNoSpace();
        for (AlloyExpr alloyExpr : this.exprs) {
            pp.begin();
            alloyExpr.pp(pp);
            if (alloyExpr != this.exprs.getLast()) {
                pp.append(COMMA);
            }
            pp.end();
            if (alloyExpr == this.exprs.getLast()) {
                pp.brk(0, -PPrinter.indentSize);
            } else {
                pp.brk();
            }
        }
        pp.append(RBRACK);
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
}
