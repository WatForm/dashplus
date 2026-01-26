/*
 * 
 * In a weird case where a transition is triggered on an internal event
 * but its guard depends on an external var value
 * it won't be triggered after a stutter
 */

// low priority no environmental stutter predicate

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

public class StutterD2A extends InitsD2A {    

    protected StutterD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addStutter() {

        
        List<AlloyExpr> body = new ArrayList<AlloyExpr>();
        
        // all the dsh defined parts of the snapshot stay the same
        if (this.dm.hasConcurrency()) 
            body.add(this.noChange(D2AStrings.stableName));
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            if (!this.dm.hasOnlyOneState())
                body.add(this.noChange(D2AStrings.confName+Integer.toString(i)));
            if (this.dm.hasConcurrency()) 
                body.add(noChange(D2AStrings.scopesUsedName+Integer.toString(i)));
                 
            body.add(AlloyEqual(this.dsl.nextTransTaken(i), this.dsl.noneArrow(i)));

            if (this.dm.hasEvents() && this.dm.hasIntEventsAti(i))
                // internal events go away
                // external events can be added
                body.add(AlloyEqual(
                                    AlloyRangeRes(
                                        this.dsl.nextEvents(i),
                                        this.dsl.allIntEventsVar()), 
                                    this.dsl.noneArrow(i)));
        }

        // internal vars
        // stays the same for all parameter values
        // so can just say that the whole relation is the same and no parameter values needed !!
        for (String vfqn: this.dm.allIntVarNames()) 
            body.add(this.noChange(vfqn));

        // buffers are all internal
        for (String bfqn: this.dm.allBufferNames()) 
            body.add(this.noChange(bfqn));

        this.addPred(D2AStrings.stutterName, this.dsl.curNextDecls(), body);

    }

    private AlloyExpr noChange(String n) {
        return AlloyEqual(
                this.dsl.nextJoinExpr(
                    AlloyVar(DashFQN.translateFQN(n))), 
                    this.dsl.curJoinExpr(AlloyVar(DashFQN.translateFQN(n))));
    }
}