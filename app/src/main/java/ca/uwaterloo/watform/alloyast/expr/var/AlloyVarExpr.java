package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public class AlloyVarExpr extends AlloyExpr {
    public final String label;

    public AlloyVarExpr(Pos pos, String label) {
        super(pos);
        this.label = label;
    }

    public AlloyVarExpr(String label) {
        super();
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.getLabel());
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
