package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.dashast.DashStrings.sendName;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

// send P[x]/ev1
// send ev1
// send y.x.ev1

public class DashSend extends DashNamedExpr implements DashTransItem {

    public DashSend(Pos pos, AlloyExpr e) {
        super(pos, e);
    }

    @Override
    public void pp(PrintContext pCtx) {
        super.pp(pCtx, sendName);
    }
}
