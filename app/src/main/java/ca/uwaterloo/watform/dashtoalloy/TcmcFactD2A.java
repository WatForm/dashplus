/*

    open util/tcmc[Snapshot] as snapshot

    fact tcmc {
        // ksS0 satisfies init (initial) constraints
        (all s: Snapshot | { s in tcmc/ks_s0 } <=> init[s])
        // ksSigma satisfies small_step predicate
        (all s,s_next: Snapshot | ({ s -> s_next } in tcmc/ks_sigma) <=> small_step[s, s_next ])
    }

*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class TcmcFactD2A extends TracesFactD2A {

    protected TcmcFactD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addTcmcFact() {

        assert (this.isTcmc);

        // open util/tcmc[Snapshot] as snapshot
        this.am.addImport(
                List.of(AlloyStrings.utilName, D2AStrings.tcmcName),
                D2AStrings.snapshotName,
                D2AStrings.snapshotName);

        List<AlloyExpr> body = this.dsl.emptyExprList();

        List<AlloyDecl> decls = this.dsl.emptyDeclList();
        decls.add(this.dsl.curDecl());

        List<AlloyExpr> args = this.dsl.emptyExprList();
        args.add(this.dsl.curVar());
        body.add(
                AlloyAllVars(
                        decls,
                        AlloyIff(
                                AlloyIn(
                                        this.dsl.curVar(),
                                        AlloyVar(D2AStrings.tcmcInitialStateName)),
                                AlloyPredCall(D2AStrings.initFactName, args))));

        body.add(
                AlloyAllVars(
                        this.dsl.curNextDecls(),
                        AlloyIff(
                                AlloyIn(
                                        AlloyArrow(this.dsl.curVar(), this.dsl.nextVar()),
                                        AlloyVar(D2AStrings.tcmcSigmaName)),
                                AlloyPredCall(D2AStrings.smallStepName, this.dsl.curNextVars()))));

        this.am.addFact(D2AStrings.tcmcFactName, body);
    }
}
