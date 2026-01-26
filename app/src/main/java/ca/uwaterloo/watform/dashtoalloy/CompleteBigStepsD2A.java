/*
 * completeBigStepsPred
 *
 * Every non-stable snapshot included in the next snapshot relation 
 * has a next snapshot, which means every big step must be complete.
 * fact completeBigSteps { 
 *      all s : Snapshot | s.stable == False 
 *			=> some s': Snapshot. small_step[s,s']
 * }
 *
 * The above works for all methods.
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

public class CompleteBigStepsD2A extends ReachabilityD2A {

    protected CompleteBigStepsD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

	public void addCompleteBigSteps() {
		if (this.dm.hasConcurrency()) {
			AlloyExpr b = 
				AlloyAllVars(this.dsl.curDecls(),
					AlloyImplies(
						this.dsl.curStableFalse(),
						AlloySomeVars(
							this.dsl.nextDecls(), 
							AlloyPredCall(D2AStrings.smallStepName, this.dsl.curNextVars()))));

			List<AlloyExpr> body = this.dsl.emptyExprList();
			body.add(b);
			this.addFact(D2AStrings.completeBigStepsName, body);
		}
	}
}
