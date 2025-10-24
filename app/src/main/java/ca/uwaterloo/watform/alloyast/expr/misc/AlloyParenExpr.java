package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyParenExpr extends AlloyExpr {
    public final AlloyExpr sub;

    public AlloyParenExpr(Pos pos, AlloyExpr sub) {
        super(pos);
        this.sub = sub;
    }

    public AlloyParenExpr(AlloyExpr sub) {
        super();
        this.sub = sub;
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
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
