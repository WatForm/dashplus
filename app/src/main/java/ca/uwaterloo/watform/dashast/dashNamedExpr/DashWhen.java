package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.whenName;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public class DashWhen extends DashNamedExpr implements DashTransItem {
    // public Expr when;
    public DashWhen(Pos pos, AlloyExpr w) {
        super(pos, w);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(whenName);
        pCtx.append(SPACE);
        if (this.exp instanceof AlloyBlock)
            exp.pp(pCtx); // will put braces around it if it is a block
        else {
            pCtx.append(LBRACE);
            pCtx.nl();
            this.exp.ppNewBlock(pCtx);
            pCtx.nl();
            pCtx.append(RBRACE);
        }
    }
}
