/*
 * Used optionally only in TCMC
 *
 * Every snapshot is reachable from an initial snapshot
 * pred reachability {
 *      all s : Snapshot | s in Snapshot .(( initial) <: * (sigma))
 * }
 */

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyReflTransClosExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class ReachabilityD2A extends ElectrumFactD2A {

    protected ReachabilityD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    public void addReachability() {
        assert (!this.isElectrum && !this.isTraces);
        AlloyExpr b =
                AlloyAllVars(
                        this.dsl.curDecls(),
                        AlloyIn(
                                this.dsl.curVar(),
                                AlloyJoin(
                                        AlloyVar(D2AStrings.snapshotName),
                                        AlloyDomainRes(
                                                AlloyVar(D2AStrings.tcmcInitialStateName),
                                                new AlloyReflTransClosExpr(
                                                        AlloyVar(D2AStrings.tcmcSigmaName))))));

        List<AlloyExpr> body = this.dsl.emptyExprList();
        body.add(b);
        // this.am.addPred(D2AStrings.reachabilityName, this.dsl.emptyDeclList(), body);
        this.am.addFact(D2AStrings.reachabilityName, body);
    }
}
