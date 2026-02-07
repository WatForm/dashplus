package ca.uwaterloo.watform.dashast.dashref;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;
import java.util.List;

public class VarDashRef extends DashRef {

    public final boolean isNext;

    public VarDashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p, n, prmValues);
        this.isNext = false;
    }

    public VarDashRef(String n, List<? extends AlloyExpr> prmValues) {
        super(n, prmValues);
        this.isNext = false;
    }

    public VarDashRef(Pos p, List<AlloyNameExpr> names, List<? extends AlloyExpr> prmValues) {
        super(p, names, prmValues);
        this.isNext = false;
    }

    private VarDashRef(String n, List<? extends AlloyExpr> prmValues, boolean isNext) {
        super(n, prmValues);
        this.isNext = isNext;
    }

    // only way to make a "next" DashRef (which happens in resolving)
    public DashRef makeNext() {
        assert (!this.isNext);
        return new VarDashRef(this.name, this.paramValues, true);
    }

    public DashStrings.DashRefKind kind() {
        return DashStrings.DashRefKind.VAR;
    }
}
