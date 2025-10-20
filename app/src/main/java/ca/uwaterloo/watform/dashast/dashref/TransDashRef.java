package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class TransDashRef extends DashRef {

    public TransDashRef(Pos p, String n, List<AlloyExpr> prmValues) {
        super(p, DashRefKind.TRANS, n, prmValues);
    }

    public TransDashRef(String n, List<AlloyExpr> prmValues) {
        super(DashRefKind.TRANS, n, prmValues);
    }
}
