/*
 * Optional pred for single "input" assumption
 * For Dash+, this means only one env event per big step
 *
 *
 *
 *  pred single_input {
 *      all s: Snapshot |
 *           lone s.events0 :> EnvEvents and no s.events1:> EnvEvents and no.events2:> EnvEvents or ...
 *           no s.events0:> EnvEvents and lone s.events1:> EnvEvents and no s.events2 :> EnvEventsor ...
 *           no s.events0 :> EnvEvents and no s.events1:> EnvEvents and lone s.events2:> EnvEvents or ...
 *  }
 */

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class SingleEventInputD2A extends EnoughOpsD2A {

    protected SingleEventInputD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addSingleEventInput() {
        if (this.dm.hasEnvEvents()) {
            AlloyExpr e;
            AlloyExpr b = AlloyFalseCond();
            for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
                if (this.dm.hasEventsAti(i)) {
                    e = AlloyTrueCond();
                    for (int j = 0; j <= this.dm.maxDepthParams(); j++) {
                        if (this.dm.hasEventsAti(j) & this.dm.hasEnvEvents()) {
                            if (i == j) {
                                e =
                                        AlloyAnd(
                                                e,
                                                AlloyLone(
                                                        AlloyRangeRes(
                                                                this.dsl.curEvents(i),
                                                                this.dsl.allEnvEventsVar())));
                            } else {
                                e =
                                        AlloyAnd(
                                                e,
                                                AlloyNo(
                                                        AlloyRangeRes(
                                                                this.dsl.curEvents(i),
                                                                this.dsl.allEnvEventsVar())));
                            }
                        }
                    }
                    b = AlloyOr(b, e);
                }
            }
            List<AlloyExpr> body = this.dsl.emptyExprList();
            if (this.isElectrum) body.add(b);
            else body.add(AlloyAllVars(this.dsl.curDecls(), b));
            this.addPred(D2AStrings.singleEventName, this.dsl.emptyDeclList(), body);
        }
    }
}
