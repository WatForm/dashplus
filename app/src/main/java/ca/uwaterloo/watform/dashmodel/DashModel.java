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

    public void debug() {
        System.out.println(stToString());
        System.out.println(ttToString());
        System.out.println(etToString());
        System.out.println(vtToString());
        System.out.println(btToString());
        System.out.println(ptToString());
    }

    public void debug(String tfqn) {
        if (tfqn != null) {
            System.out.println("src " + fromR(tfqn));
            System.out.println("dest " + gotoR(tfqn));
            System.out.println("pre " + whenR(tfqn));
            System.out.println("post " + doR(tfqn));
            System.out.println("getScope " + scope(tfqn));
            System.out.println("getClosestParamAnces: " + closestParamAnces(fromR(tfqn).name));
            // System.out.println("getAllNonParamDesc: "
            // +getAllNonParamDesc(getClosestConcAnces(getTransSrc(tfqn).getName())));
            System.out.println("getRegion:" + "Root/S1/S2: " + region(fromR(tfqn).name));
            System.out.println("exited: " + exited(tfqn));
            System.out.println("entered" + leafStatesEntered(gotoR(tfqn)));
            System.out.println("enteredInScope" + entered(tfqn));
            System.out.println("allPrefixDashRefs of scope: " + prefixDashRefs(scope(tfqn)));
            System.out.println("scopesUsed: " + scopesUsed(tfqn));
            System.out.println("nonOrthogonalScopes: " + nonOrthogonalScopesOf(tfqn));
        }
    }
}
