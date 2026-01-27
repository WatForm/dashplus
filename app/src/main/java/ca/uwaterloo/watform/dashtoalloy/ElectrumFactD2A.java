/*
    fact electrum {
        init and always small_step
    }
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyAlwaysExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class ElectrumFactD2A extends TcmcFactD2A {

    protected ElectrumFactD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addElectrumFact() {

        assert (this.isElectrum);
        List<AlloyExpr> body = this.dsl.emptyExprList();

        body.add(AlloyVar(D2AStrings.initFactName));
        body.add(new AlloyAlwaysExpr(AlloyVar(D2AStrings.smallStepName)));
        this.addFact(D2AStrings.initFactName, body);
    }
}
