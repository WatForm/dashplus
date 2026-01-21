/*
 	Everything added to a DashModel must be
 	fully resolved.

 	Inv and Init can only be added at the top-level (not in substates).

	Have to get a new ExprRefResolver for each addition
	b/c a previous add may have added something new.

    All model setters should be methods (not directly setting an attribute)

    NADTODO: what checks do there need to be on what's added?

*/

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import java.util.HashMap;
import java.util.List;

public class DashModel extends ResolveDM {

    public DashModel(DashFile d) {
        super(d);
    }

    public DashModel() {
        super();
    }

    // a couple of cross-table functions

    public List<String> namesOfState(String sfqn) {
        List<String> x = varsOfState(sfqn);
        x.addAll(buffersOfState(sfqn));
        return x;
    }

    public List<String> varAndBufferNames() {
        // vars plus buffers
        List<String> x = allVarNames();
        x.addAll(allBufferNames());
        return x;
    }

    // setters

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
        addEvent(efqn, k, prms);
        return;
    }

    public void addVar(String vfqn, DashStrings.IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        // TODO what if prms don't exist??
        addVar(vfqn, k, prms, t);
        return;
    }

    public void addInv(AlloyExpr inv) {
        invsR.add(inv);
        return;
    }

    public void addInit(AlloyExpr init) {
        initsR.add(init);
        return;
    }

    public void cloneEventTableIn(DashModel dm) {
        // this will overwrite any existing StateTable in dm
        dm.et = new HashMap<>(this.et);
    }

    public void cloneStateTableIn(DashModel dm) {
        // this will overwrite any existing StateTable in dm
        dm.st = new HashMap<>(this.st);
    }
}
