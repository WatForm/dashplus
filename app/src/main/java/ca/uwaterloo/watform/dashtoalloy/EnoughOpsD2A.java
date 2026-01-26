
/*
 * There exists at least one representative of every transition
 *
 * pred operationsSignificance { 
 *     some s, s’: Snapshot | some p0, p1 | T1[s, s’] 
 *     some s, s’: Snapshot | T2[s, s’] 
 *     some s, s’: Snapshot | T3[s, s’] 
 *     ...
 * }
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

public class EnoughOpsD2A extends AllSnapshotsDiffD2A {    

    protected EnoughOpsD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addEnoughOps() {

        ArrayList<AlloyExpr> body = new ArrayList<AlloyExpr>();

        for (String tfqn: this.dm.allTransNames()) {
            String tout = DashFQN.translateFQN(tfqn);
            List<DashParam> prms =  this.dm.transParams(tfqn);
            if (prms.size() == 0) {
                if (this.isElectrum) {
                    List<AlloyExpr> elist = new ArrayList<AlloyExpr>();
                    body.add(AlloyPredCall(tout, elist));
                } else 
                    body.add(
                        AlloySomeVars(
                            this.dsl.curNextDecls(),
                            AlloyPredCall(tout,this.dsl.curNextVars())));
            } else {
                body.add(AlloySomeVars(
                    this.dsl.curNextParamsDecls(prms),
                    AlloyPredCall(tout,this.dsl.curNextParamVars(prms))));
            }
        }
        ArrayList<AlloyDecl> nodecls = new ArrayList<AlloyDecl>();
        this.addPred(D2AStrings.enoughOpsName,nodecls, body);
    }
}
