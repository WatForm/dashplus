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
import java.util.stream.Collectors;
import java.util.List;

public class TestIfNextStableD2A extends TransPreD2A {

    protected TestIfNextStableD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    // only one of these predicates per model
    public void addTestIfNextStable() {
        
        List<AlloyExpr> body = this.dsl.emptyExprList();
        List<AlloyDecl> decls = this.dsl.emptyDecls();
        

        if (!this.isElectrum) {
            decls.addAll(this.dsl.curNextDecls());
        }

        for (DashParam p: this.dm.allParamsInOrder()) {
            decls.add(p.asAlloyDecl());
        }

        Integer maxDepthParams = this.dm.maxDepthParams();
        for (int i=0; i<= maxDepthParams; i++) {
            decls.add(this.dsl.scopeDecl(i));
            if (this.dm.hasEventsAti(i)) {
                decls.add(this.dsl.genEventDecl(i));
            }
        }

        List<AlloyExpr> args;
        // this will include transition tfqn itself
        for (String tfqn: this.dm.allTransNames()) {

            args = this.dsl.emptyExprList();
            if (!this.isElectrum) 
                args.addAll(this.dsl.curNextVars());

            for (DashParam p:this.dm.transParams(tfqn)) {
                args.add(p.asAlloyVar());
            }

            for (int i=0; i<= maxDepthParams; i++) {
                args.add(this.dsl.scopeVar(i));
                if (this.dm.hasEventsAti(i)) {
                    args.add(this.dsl.genEventVar(i));
                }
            }

            String tout = DashFQN.translateFQN(tfqn);
            body.add(
                AlloyNot(
                    AlloyPredCall(
                        tout + D2AStrings.enabledAfterStepName, 
                        args)));
        }
        this.addPred(D2AStrings.testIfNextStableName,decls,body);
    }
}