package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStateItem;
import ca.uwaterloo.watform.utils.*;

public final class DashInv extends DashNamedExpr implements DashStateItem {

    public final String name;

    public DashInv(Pos p, AlloyExpr inv) {
        super(p, inv);
        this.name = "";
    }

    public DashInv(Pos p, String n, AlloyExpr inv) {
        super(p, inv);
        assert (n != "");
        assert (n != null);
        this.name = n;
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(invName + SPACE);
        if (!this.name.isEmpty()) {
            pCtx.append(name + SPACE);
        }
        pCtx.append(LBRACE);
        pCtx.brkNoSpace();
        exp.ppNewBlock(pCtx);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RBRACE);
    }
}
