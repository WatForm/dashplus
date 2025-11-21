package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
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
    public List<DashInv> invsP;
    public List<DashInit> initsP;

    // calculated when resolved
    public List<AlloyExpr> invsR;
    public List<AlloyExpr> initsR;

    public StateElement(
            DashStrings.StateKind k,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren,
            List<DashInv> invP,
            List<DashInit> initP) {
        assert (k != null);
        assert (prms != null);
        assert (parent == null || !parent.isEmpty());
        assert (iChildren != null); // could be empty
        assert (invP != null);
        assert (initP != null);

        this.kind = k;
        this.params = prms;
        this.def = def;
        this.parent = parent;
        this.immChildren = iChildren;
        this.invsP = invP;
        this.initsP = initP;
    }

    public String toString() {
        String s = new String();
        s += "kind: " + this.kind + "\n";
        s += "params: " + NoneStringIfNeeded(this.params) + "\n";
        s += "default: " + this.def + "\n";
        s += "parent: " + NoneStringIfNeeded(this.parent) + "\n";
        s += "immChildren: " + NoneStringIfNeeded(this.immChildren) + "\n";
        return s;
    }
}
