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
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashParam;
import java.util.List;

public class SmallStepD2A extends TransEnabledD2A {

    protected SmallStepD2A(DashModel dm, Options opt) {
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
                e.add(
                        AlloyPredCall(
                                D2AStrings.transName(tout),
                                this.dsl.paramVars(this.dm.transParams(tfqn))));
            // p3.p2.p1.s'.s.t for parameters of this transition
            else
                e.add(
                        AlloyPredCall(
                                D2AStrings.transName(tout),
                                this.dsl.curNextParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transIsTaken;
        if (this.dm.allParams().isEmpty()) transIsTaken = AlloyOrList(e);
        else transIsTaken = AlloySomeVars(this.dsl.paramDecls(prs), AlloyOrList(e));

        AlloyExpr transIsNotEnabled =
                AlloyAnd(
                        AlloyNot(
                                AlloyPredCall(
                                        D2AStrings.transEnabledName, List.of(this.dsl.curVar()))),
                        AlloyPredCall(D2AStrings.stutterName, this.dsl.curNextVars()));

        e = this.dsl.emptyExprList();
        e.add(AlloyOr(transIsTaken, transIsNotEnabled));
        this.am.addPred(D2AStrings.smallStepName, this.dsl.curNextDecls(), e);
    }
}
