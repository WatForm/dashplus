/*
    pred t1[s:Snapshot,s':Snapshot,pparam0:param0,pparam1:param1,pparam2:param2,...] =
        pparam2.pparam1.pparam0.s.pre_1 and
        pparam2.pparam1.pparam0.s'.s.post_1
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.List;

public class TransD2A extends TransPostD2A {

    protected TransD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addTrans(String tfqn) {

        // e.g. [ClientId,ServerId.,,,]
        List<DashParam> prs = this.dm.transParams(tfqn);
        List<AlloyExpr> body = new ArrayList<AlloyExpr>();
        String tout = DashFQN.translateFQN(tfqn); // output FQN

        if (this.isElectrum) {
            // pre_transName[ p0, p1, p2] -> p2.p1.p0.pre_transName
            body.add(AlloyPredCall(tout + D2AStrings.preName, this.dsl.paramVars(prs)));
            // p2.p1.p0.post_transName
            body.add(AlloyPredCall(tout + D2AStrings.postName, this.dsl.paramVars(prs)));
        } else {
            // pre_transName[s, p0, p1, p2] -> p2.p1.p0.s.pre_transName
            body.add(AlloyPredCall(tout + D2AStrings.preName, this.dsl.curParamVars(prs)));
            // p2.p1.p0.s'.s.post_transName
            body.add(AlloyPredCall(tout + D2AStrings.postName, this.dsl.curNextParamVars(prs)));
        }
        this.addPred(tout, this.dsl.curNextParamsDecls(prs), body);
    }
}
