package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.utils.*;

public class DashDo extends DashNamedExpr implements DashTransItem {
    public DashDo(Pos pos, AlloyExpr d) {
        super(pos, d);
    }

    @Override
    public final void pp(PrintContext pCtx) {
        pCtx.append(doName);
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
