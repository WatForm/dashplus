package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyNumExpr extends AlloyVarExpr {
    public final boolean isPositive;
    public final int value;

    public AlloyNumExpr(Pos pos, boolean isPositve, String numLabel) {
        super(pos, isPositve ? numLabel : AlloyStrings.MINUS + numLabel);
        this.value = Integer.parseInt(numLabel);
        this.isPositive = isPositve;
    }

    public AlloyNumExpr(boolean isPositve, String numLabel) {
        super(isPositve ? numLabel : AlloyStrings.MINUS + numLabel);
        this.value = Integer.parseInt(numLabel);
        this.isPositive = isPositve;
    }

    public AlloyNumExpr(boolean isPositve, int num) {
        super(isPositve ? String.valueOf(num) : AlloyStrings.MINUS + String.valueOf(num));
        this.value = Integer.parseInt(String.valueOf(num));
        this.isPositive = isPositve;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isPositive ? 1231 : 1237);
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyNumExpr other = (AlloyNumExpr) obj;
        if (isPositive != other.isPositive) return false;
        if (value != other.value) return false;
        return true;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
