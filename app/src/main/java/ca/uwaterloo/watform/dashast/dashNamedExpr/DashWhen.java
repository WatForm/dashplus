package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.dashast.DashStrings.whenName;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public class DashWhen extends DashNamedExpr implements DashTransItem {
    // public Expr when;
    public DashWhen(Pos pos, AlloyExpr w) {
        super(pos, w);
    }

    @Override
    public void pp(PrintContext pCtx) {
        super.pp(pCtx, whenName);
    }
}
