package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class VarDashRef extends DashRef {

    public VarDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, DashRefKind.VAR, n, prmValues);
    }

    public VarDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(DashRefKind.VAR, n, prmValues);
    }
}
