package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class EventDashRef extends DashRef {

    public EventDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, DashRefKind.EVENT, n, prmValues);
    }

    public EventDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(DashRefKind.EVENT, n, prmValues);
    }
}
