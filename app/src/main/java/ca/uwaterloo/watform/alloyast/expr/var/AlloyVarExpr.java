package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyVarExpr extends AlloyExpr {
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

    // The given label is not used for most concrete AlloyVarExpr, like AlloyUnivExpr etc. These are
    // here
    // for the sake of completion.
    // The label is used for AlloyAtName, AlloyNameExpr, AlloyNumExpr, AlloyQnameExpr,
    // AlloyStrLiteralExpr.
    public abstract AlloyVarExpr rebuild(String label);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyVarExpr other = (AlloyVarExpr) obj;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        return true;
    }
}
