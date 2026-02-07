package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class TransDashRef extends DashRef {

    public TransDashRef(Pos p, String n, List<AlloyExpr> prmValues) {
        super(p, n, prmValues);
    }

    public TransDashRef(String n, List<AlloyExpr> prmValues) {
        super(n, prmValues);
    }

    public DashStrings.DashRefKind kind() {
        return DashStrings.DashRefKind.TRANS;
    }
}
