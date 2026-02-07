package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class EventDashRef extends DashRef {

    public EventDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, n, prmValues);
    }

    public EventDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(n, prmValues);
    }

    public EventDashRef(Pos p, List<AlloyNameExpr> names, List<? extends AlloyExpr> prmValues) {
        super(p, names, prmValues);
    }

    public DashStrings.DashRefKind kind() {
        return DashStrings.DashRefKind.EVENT;
    }
}
