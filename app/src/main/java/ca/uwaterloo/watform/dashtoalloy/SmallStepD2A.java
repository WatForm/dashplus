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
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmallStepD2A extends TransD2A {  

    protected SmallStepD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addSmallStep() {

        ArrayList<AlloyExpr> e = new ArrayList<AlloyExpr>();
        List<DashParam> prs = this.dm.allParamsInOrder();

        // trans is taken
        for (String tfqn: this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn); 
            // p3.p2.p1.t for parameters of this transition
            if (this.isElectrum) 
                e.add(AlloyPredCall(
                        tout,
                        this.dsl.paramVars(this.dm.transParams(tfqn))));
            // p3.p2.p1.s'.s.t for parameters of this transition
            else 
                e.add(AlloyPredCall(
                        tout,
                        this.dsl.curNextParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transIsTaken;
        if (this.dm.allParamsInOrder().isEmpty()) 
            transIsTaken = AlloyOrList(e);
        else 
            transIsTaken = 
                AlloySomeVars(
                    this.dsl.paramDecls(prs),
                    AlloyOrList(e));

        // no trans is enabled
        e = new ArrayList<AlloyExpr>();
        for (String tfqn: this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn); 
            // p3.p2.p1.t for parameters of this transition
            if (this.isElectrum) 
                e.add(AlloyPredCall(
                    tout,
                    this.dsl.paramVars(this.dm.transParams(tfqn))));
            // p3.p2.p1.s'.s.t for parameters of this transition
            else 
                e.add(AlloyPredCall(
                    tout+D2AStrings.preName,
                    this.dsl.curParamVars(this.dm.transParams(tfqn))));
        }
        AlloyExpr transIsNotEnabled;
        if (this.dm.allParamsInOrder().isEmpty()) 
            transIsNotEnabled = AlloyOrList(e);
        else 
            transIsNotEnabled = 
                AlloySomeVars(
                    this.dsl.paramDecls(prs),
                    AlloyOrList(e));
        transIsNotEnabled = 
            AlloyAnd(
                AlloyNot(transIsNotEnabled), 
                AlloyPredCall(
                    D2AStrings.stutterName, 
                    this.dsl.curNextVars()));

        e = new ArrayList<AlloyExpr>();
        e.add(AlloyOr(transIsTaken, transIsNotEnabled));
        this.addPred(
            D2AStrings.smallStepName,
            this.dsl.curNextDecls(),
            e);
    }
}