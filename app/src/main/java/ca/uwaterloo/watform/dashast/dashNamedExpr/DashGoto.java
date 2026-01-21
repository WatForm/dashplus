package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.dashast.DashStrings.gotoName;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public class DashGoto extends DashNamedExpr implements DashTransItem {

    public DashGoto(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public void pp(PrintContext pCtx) {
        super.pp(pCtx, gotoName);
    }
}
