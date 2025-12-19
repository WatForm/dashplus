/*
    The expression can be given a name in printing.
    This class and those below is are not needed in expression visitors
    because they are only used for parsing and printing.
*/

package ca.uwaterloo.watform.dashast.dashNamedExpr;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
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

    // Special toString for when expression has a name
    public void toString(String name, StringBuilder sb, int indent) {
        sb.append(DashStrings.indent(indent) + name + " {\n");
        sb.append(DashStrings.indent(indent + 1));
        this.exp.toString(sb, indent + 1);
        sb.append("\n" + DashStrings.indent(indent) + "}\n");
    }

    public final void pp(PrintContext pCtx, String name) {
        pCtx.append(name);
        pCtx.append(SPACE + LBRACE);
        pCtx.brkNoSpace();
        exp.ppNewBlock(pCtx);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RBRACE);
    }
}
