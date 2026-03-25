/*
    The expression can be given a name in printing.
    This class and those below is are not needed in expression visitors
    because they are only used for parsing and printing.
*/

package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public abstract class DashNamedExpr extends ASTNode {

    public final AlloyExpr exp;

    public DashNamedExpr(Pos pos, AlloyExpr exp) {
        super(pos);
        this.exp = exp;
    }

    public DashNamedExpr() {
        super();
        this.exp = null;
    }

    public final void pp(PrintContext pCtx, String name) {
        pCtx.append(name);
        pCtx.append(SPACE);
        exp.pp(pCtx); // will put braces around it if it is a block
    }
}
