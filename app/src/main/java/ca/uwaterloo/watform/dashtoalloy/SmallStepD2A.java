/*
    pred small_step [s:Snapshot, s': Snapshot] {
            (some pparam0 : Param0 , pparam1 : Param1 ... |
                { for all t’s at level i with params Param5, Param6, ...
                (or t[s, s_next, pparam5, pparam6 ])
                })
            or
            (!(some pparam0 : Param0 , pparam1 : Param1 ... |
                { for all t’s at level i with params Param5, Param6, ...
                (or t_pre[s, s_next, pparam5, pparam6 ]) )
                and s = s')

*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class SmallStepD2A extends TransD2A {

    protected SmallStepD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addSmallStep() {

        List<AlloyExpr> e = this.dsl.emptyExprList();
        List<DashParam> prs = this.dm.allParams();

        // trans is taken
        for (String tfqn : this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn);
            // p3.p2.p1.t for parameters of this transition
            if (this.isElectrum)
                e.add(AlloyPredCall(tout, this.dsl.paramVars(this.dm.transParams(tfqn))));
            // p3.p2.p1.s'.s.t for parameters of this transition
            else e.add(AlloyPredCall(tout, this.dsl.curNextParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transIsTaken;
        if (this.dm.allParams().isEmpty()) transIsTaken = AlloyOrList(e);
        else transIsTaken = AlloySomeVars(this.dsl.paramDecls(prs), AlloyOrList(e));

        // no trans is enabled
        e = this.dsl.emptyExprList();
        for (String tfqn : this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn);
            // p3.p2.p1.t for parameters of this transition
            if (this.isElectrum)
                e.add(AlloyPredCall(tout, this.dsl.paramVars(this.dm.transParams(tfqn))));
            // p3.p2.p1.s'.s.t for parameters of this transition
            else
                e.add(
                        AlloyPredCall(
                                tout + D2AStrings.preName,
                                this.dsl.curParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transIsNotEnabled;
        if (this.dm.allParams().isEmpty()) transIsNotEnabled = AlloyOrList(e);
        else transIsNotEnabled = AlloySomeVars(this.dsl.paramDecls(prs), AlloyOrList(e));
        transIsNotEnabled =
                AlloyAnd(
                        AlloyNot(transIsNotEnabled),
                        AlloyPredCall(D2AStrings.stutterName, this.dsl.curNextVars()));

        e = this.dsl.emptyExprList();
        e.add(AlloyOr(transIsTaken, transIsNotEnabled));
        this.am.addPred(D2AStrings.smallStepName, this.dsl.curNextDecls(), e);
    }
}
