package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.dashast.DashStrings.initName;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStateItem;
import ca.uwaterloo.watform.utils.*;

public final class DashInit extends DashNamedExpr implements DashStateItem {

    public DashInit(Pos p, AlloyExpr e) {
        super(p, e);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(initName, sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        super.pp(pCtx, initName);
    }
}
