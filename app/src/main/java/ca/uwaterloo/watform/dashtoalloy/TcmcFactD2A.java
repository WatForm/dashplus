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
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyAlwaysExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashast.dashref.TransDashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class TcmcFactD2A extends InitsD2A {    

    protected TcmcFactD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addTcmcFact() {

        assert(this.isTcmc);

        // TODO add open statement

        List<AlloyExpr> body = this.dsl.emptyExprList();
 
        List<AlloyDecl> decls = this.dsl.emptyDecls();
        decls.add(this.dsl.curDecl());

        List<AlloyExpr> args = this.dsl.emptyExprList();
        args.add(this.dsl.curVar());
        body.add(
            AlloyAllVars(
                decls, 
                AlloyIff(
                    AlloyIn(this.dsl.curVar(), AlloyVar(D2AStrings.tcmcInitialStateName)),
                    AlloyPredCall(D2AStrings.initFactName,args))));

        body.add(
            AlloyAllVars(
                this.dsl.curNextDecls(), 
                AlloyIff(
                    AlloyIn(
                        AlloyArrow(this.dsl.curVar(), this.dsl.nextVar()),
                        AlloyVar(D2AStrings.tcmcSigmaName)),
                    AlloyPredCall(D2AStrings.smallStepName, this.dsl.curNextVars()))));

        this.addFact(D2AStrings.tcmcFactName, body);

    }
}