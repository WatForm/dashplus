/*
    The expression can be given a name in printing.
    This class and those below is are not needed in expression visitors
    because they are only used for parsing and printing.
*/

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public abstract class DashNamedExpr extends ASTNode {

    public AlloyExpr exp;

    public DashNamedExpr(Pos pos, AlloyExpr exp) {
        super(pos);
        exp = exp;
    }

    public DashNamedExpr() {
        super();
    }

    // Special toString for when expression has a name
    public void toString(String name, StringBuilder sb, int indent) {
        sb.append(DashStrings.indent(indent) + name + " {\n");
        this.toString(sb, indent);
        sb.append("\n" + DashStrings.indent(indent) + "}\n");
    }
}
