package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.dashast.dashref.*;

public interface DashExprVis<T> extends AlloyExprVis<T> {

    public default T visit(DashExpr expr) {
        return expr.accept(this);
    }

    T visit(DashRef ref);

    T visit(DashParam dashParam);
}
