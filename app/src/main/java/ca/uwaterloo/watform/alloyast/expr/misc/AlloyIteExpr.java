package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyIteExpr extends AlloyExpr {
    public final AlloyExpr cond;
    public final AlloyExpr conseq;
    public final AlloyExpr alt;

    public AlloyIteExpr(Pos pos, AlloyExpr cond, AlloyExpr conseq, AlloyExpr alt) {
        super(pos);
        this.cond = cond;
        this.conseq = conseq;
        this.alt = alt;
    }

    public AlloyIteExpr(AlloyExpr cond, AlloyExpr conseq, AlloyExpr alt) {
        super();
        this.cond = cond;
        this.conseq = conseq;
        this.alt = alt;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.cond.toString(sb, indent);
        sb.append(AlloyStrings.SPACE);
        sb.append(AlloyStrings.RFATARROW);
        sb.append(AlloyStrings.SPACE);
        this.conseq.toString(sb, indent);
        sb.append(AlloyStrings.SPACE);
        sb.append(AlloyStrings.ELSE);
        sb.append(AlloyStrings.SPACE);
        this.alt.toString(sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
