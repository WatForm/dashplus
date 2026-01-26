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
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElectrumFactD2A extends TcmcFactD2A {    

    protected ElectrumFactD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addElectrumFact() {

        assert(this.isElectrum);
        List<AlloyExpr> body = new ArrayList<AlloyExpr>();
        
        body.add(AlloyVar(D2AStrings.initFactName));
        body.add(new AlloyAlwaysExpr(AlloyVar(D2AStrings.smallStepName)));
        this.addFact(D2AStrings.initFactName, body);

    }

}
