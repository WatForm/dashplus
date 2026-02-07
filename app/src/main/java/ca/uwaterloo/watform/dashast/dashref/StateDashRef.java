package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class StateDashRef extends DashRef {

    public StateDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, n, prmValues);
    }

    public StateDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(n, prmValues);
    }

    public StateDashRef(Pos p, List<AlloyNameExpr> names, List<? extends AlloyExpr> prmValues) {
        super(p, names, prmValues);
    }

    public DashStrings.DashRefKind kind() {
        return DashStrings.DashRefKind.STATE;
    }
}
