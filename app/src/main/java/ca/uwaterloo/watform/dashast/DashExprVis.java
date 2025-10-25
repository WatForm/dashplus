package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.dashast.dashref.*;

public interface DashExprVis<T> extends AlloyExprVis<T> {

    T visit(DashRef ref);

    T visit(DashParam dashParam);
}
