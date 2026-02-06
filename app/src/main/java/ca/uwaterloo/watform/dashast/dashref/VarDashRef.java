package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class VarDashRef extends DashRef {

    public VarDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, DashStrings.DashRefKind.VAR, n, prmValues);
    }

    public VarDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(DashStrings.DashRefKind.VAR, n, prmValues);
    }

    public VarDashRef(String n, List<? extends AlloyExpr> prmValues, boolean isNext) {
        super(DashStrings.DashRefKind.VAR, n, prmValues, isNext);
    }
}
