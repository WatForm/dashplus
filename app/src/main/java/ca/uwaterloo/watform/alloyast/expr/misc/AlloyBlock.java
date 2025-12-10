package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class AlloyBlock extends AlloyExpr {
    public final List<AlloyExpr> exprs;

    public AlloyBlock(Pos pos, List<AlloyExpr> exprs) {
        super(pos);
        this.exprs = Collections.unmodifiableList(exprs);
    }

    public AlloyBlock(List<AlloyExpr> exprs) {
        super();
        this.exprs = Collections.unmodifiableList(exprs);
    }

    public AlloyBlock(Pos pos, AlloyExpr expr) {
        super(pos);
        this.exprs = Collections.unmodifiableList(Collections.singletonList(expr));
    }

    public AlloyBlock(AlloyExpr expr) {
        super();
        this.exprs = Collections.unmodifiableList(Collections.singletonList(expr));
    }

    public AlloyBlock() {
        super();
        this.exprs = Collections.emptyList();
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String tabs = TAB.repeat(indent);

        sb.append(LBRACE);

        for (AlloyExpr expr : this.exprs) {
            sb.append(NEWLINE + tabs + TAB);
            expr.toString(sb, indent + 1);
        }
        sb.append(NEWLINE);

        sb.append(tabs);
        sb.append(RBRACE);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.exprs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyBlock other = (AlloyBlock) obj;
        if (exprs == null) {
            if (other.exprs != null) return false;
        } else if (!exprs.equals(other.exprs)) return false;
        return true;
    }

    @Override
    public void pp(PPrinter pp) {
        pp.append(LBRACE);
        pp.brk();

        for (AlloyExpr alloyExpr : this.exprs) {
            pp.begin();
            alloyExpr.pp(pp);
            pp.end();
            if (alloyExpr == this.exprs.getLast()) {
                pp.nlNoIndent();
            } else {
                pp.nl();
            }
        }

        pp.append(RBRACE);
    }
}
