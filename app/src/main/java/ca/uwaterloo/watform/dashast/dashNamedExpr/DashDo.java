package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.dashast.DashStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public class DashDo extends DashNamedExpr implements DashTransItem {
    public DashDo(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(doName, sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        super.pp(pCtx, doName);
    }
}
