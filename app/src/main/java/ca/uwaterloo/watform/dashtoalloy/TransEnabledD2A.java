package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashParam;
import java.util.List;

public class TransEnabledD2A extends TransD2A {

    protected TransEnabledD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    public void addTransEnabled() {
        List<AlloyExpr> e = this.dsl.emptyExprList();
        List<DashParam> prs = this.dm.allParams();

        // no trans is enabled
        e = this.dsl.emptyExprList();
        for (String tfqn : this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn);

            if (this.isElectrum)
                // TODO: this is wrong
                e.add(AlloyPredCall(tout, this.dsl.paramVars(this.dm.transParams(tfqn))));
            else
                // __tout_pre[s, sn, p0, p1]
                e.add(
                        AlloyPredCall(
                                D2AStrings.preName(tout),
                                this.dsl.curParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transEnabled;

        if (this.dm.allParams().isEmpty())
            // __t1_pre[s, sn, p0, p1] or __t2_pre[s, sn, p0, p1] or ...
            transEnabled = AlloyOrList(e);
        else
            // some p0, p2 | __t1_pre[s, sn, p0, p1] or __t2_pre[s, s', p0, p1] or ...
            transEnabled = AlloySomeVars(this.dsl.paramDecls(prs), AlloyOrList(e));
        // __transNotEnabled[s,sn] = some p0, p2 | __t1_pre[s, sn, p0, p1] or __t2_pre[s, s', p0,
        // p1] or ...
        this.am.addPred(D2AStrings.transEnabledName, this.dsl.curDecls(), List.of(transEnabled));
    }
}
