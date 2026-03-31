/*
    The expression can be given a name in printing.
    This class and those below is are not needed in expression visitors
    because they are only used for parsing and printing.
*/

package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public abstract class DashNamedExpr extends ASTNode {

    public final AlloyExpr exp;

    public DashNamedExpr(Pos pos, AlloyExpr exp) {
        super(pos);
        this.exp = exp;
        reqNonNull(nullField(pos, this), this.exp);
    }

    public DashNamedExpr() {
        super();
        this.exp = null;
    }

    public final void pp(PrintContext pCtx, String name) {
        pCtx.append(name);
        pCtx.append(SPACE);
        this.exp.ppNewBlock(pCtx);
    }
}
