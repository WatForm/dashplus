package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class StateDashRef extends DashRef {

    public StateDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, DashRefKind.STATE, n, prmValues);
    }

    public StateDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(DashRefKind.STATE, n, prmValues);
    }
}
