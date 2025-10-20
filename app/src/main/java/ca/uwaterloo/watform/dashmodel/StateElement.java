package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import java.util.List;

public class StateElement {

    public DashStrings.StateKind kind;
    // empty is none; param of this state is last of params if it exists
    public List<DashParam> params; // only DashParam's
    public DashStrings.DefKind def;

    // these all use FQNs to point to trans in TransTable
    // or states in this StateTable
    public String parent; // null if none
    public List<String> immChildren; // empty if none
    public List<DashInv> origInvs;
    public List<DashInit> origInits;

    // calculated when resolved
    public List<AlloyExpr> invs;
    public List<AlloyExpr> inits;

    public StateElement(
            DashStrings.StateKind k,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren,
            List<DashInv> invL,
            List<DashInit> initL) {
        assert (k != null);
        assert (prms != null);
        assert (parent == null || !parent.isEmpty());
        assert (iChildren != null); // could be empty
        assert (invL != null);
        assert (initL != null);

        this.kind = k;
        this.params = prms;
        this.def = def;
        this.parent = parent;
        this.immChildren = iChildren;
        this.origInvs = invL;
        this.origInits = initL;
    }

    public String toString() {
        String s = new String();
        s += "kind: " + kind + "\n";
        s += "params: " + NoneStringIfNeeded(params) + "\n";
        s += "default: " + def + "\n";
        s += "parent: " + NoneStringIfNeeded(parent) + "\n";
        s += "immChildren: " + NoneStringIfNeeded(immChildren) + "\n";
        return s;
    }
}
