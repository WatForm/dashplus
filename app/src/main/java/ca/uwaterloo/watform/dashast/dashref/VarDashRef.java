/*
    for vars or buffers -- DashRefs used in expressions
*/

package ca.uwaterloo.watform.dashast.dashref;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;
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

    @Override
    public void pp(PrintContext pCtx) {
        // pp within Dash state (not in AlloyModel, where it should not exist)
        //  Root/A/B[a1,b1]/var1 or Root/A/B[a1,b1]/var1'
        String s = "";
        if (!this.paramValues.isEmpty()) {
            // then it has to be at least partially resolved already
            s += DashFQN.chopPrefixFromFQN(this.name);
            s += "[";
            s += GeneralUtil.strCommaList(this.paramValues);
            s += "]/";
            s += DashFQN.chopNameFromFQN(this.name);
        } else {
            s += this.name;
        }
        if (this.isNext) s += DashStrings.PRIME;

        pCtx.append(s);
    }
}
