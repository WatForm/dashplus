/* optional for tcmc only

    pred no_stutter {
        all s:DshSnapshot |
        s = tcmc/ks_s0 or 
        NO_TRANS not in s.dsh_taken0 or
        all p:Identifiers | NO_TRANS not in s.(p.dsh_taken0)
        ... for all levels
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

public class StrongNoStutterD2A extends InitsD2A {    

    protected StrongNoStutterD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addStrongNoStutter() {

        assert(this.isTcmc);
        AlloyExpr snapShotFirst = AlloyVar(D2AStrings.tcmcInitialStateName);
        List<AlloyExpr> body = this.dsl.emptyExprList();
        List<AlloyDecl> decls = this.dsl.emptyDecls();
 
        List<AlloyExpr> bigOr = this.dsl.emptyExprList();
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            // don't need to make this stronger than an Or
            // b/c other parts of semantics will make sure only
            // one transTaken is true
            bigOr.add(
                AlloyNot(
                    AlloyEqual(
                        this.dsl.curTransTaken(i), 
                        this.dsl.noneArrow(i))));
        }
        bigOr.add(AlloyEqual(
                        this.dsl.curVar(), 
                        snapShotFirst));
        
        decls.add(this.dsl.curDecl());
        body.add(AlloyAllVars(decls,AlloyOrList(bigOr)));

        this.addPred(D2AStrings.strongNoStutterName, this.dsl.emptyDecls(), body);

    }
}