/*
 	Everything added to a DashModel must be
 	fully resolved.

 	Inv and Init can only be added at the top-level (not in substates).

	Have to get a new ExprRefResolver for each addition
	b/c a previous add may have added something new.

    All model setters should be methods (not directly setting an attribute)

*/

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import java.util.List;

public class DashModel extends DashModelAccessors {

    public DashModel(DashFile d) {
        super(d);
    }

    public void setRootName(String sfqn) {
        st.root = sfqn;
    }

    public void addState(
            DashStrings.StateKind k,
            List<DashParam> prms,
            DashStrings.DefKind def,
            String parent,
            List<String> iChildren) {
        // TODO children must exist
        // TODO what if prms don't exist??
        return;
    }

    public void addEvent(String efqn, DashStrings.IntEnvKind k, List<DashParam> prms) {
        // TODO check if prefix name is an existing state with the same prms
        et.add(efqn, k, prms);
        return;
    }

    public void addVar(String vfqn, DashStrings.IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        // TODO what if prms don't exist??
        vt.add(vfqn, k, prms, t);
        return;
    }

    public void addInv(AlloyExpr inv) {
        ExprRefResolverVis er = new ExprRefResolverVis(st, tt, et, vt, bt, pt);
        st.invsR.add(er.visit(inv));
        return;
    }

    public void addInit(AlloyExpr init) {
        ExprRefResolverVis er = new ExprRefResolverVis(st, tt, et, vt, bt, pt);
        st.initsR.add(er.visit(init));
        return;
    }
}
