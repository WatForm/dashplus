package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNumExpr extends AlloyVarExpr {
    public final int value;

    public AlloyNumExpr(Pos pos, String numLabel) {
        super(pos, numLabel);
        this.value = Integer.parseInt(numLabel);
    }

    public AlloyNumExpr(String numLabel) {
        this(Pos.UNKNOWN, numLabel);
    }

    public AlloyNumExpr(int num) {
        this(Pos.UNKNOWN, String.valueOf(num));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((0 <= this.value) ? 1231 : 1237);
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyNumExpr other = (AlloyNumExpr) obj;
        if (value != other.value) return false;
        return true;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyNumExpr rebuild(String label) {
        return new AlloyNumExpr(this.pos, label);
    }
}
