/*
	These classes are used only during parsing because
	we do not know what order items within a state will be parsed in.

    The expression can be given a name in printing.
*/

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public abstract class DashExpr extends AlloyExpr {

    public DashExpr(Pos pos, AlloyExpr exp) {
        super(pos);
    }

    public DashExpr() {
        super();
    }

    // Special toString for when expression has a name
    public void toString(String name, StringBuilder sb, int indent) {
        sb.append(DashStrings.indent(indent) + name + " {\n");
        this.toString(sb, indent);
        sb.append("\n" + DashStrings.indent(indent) + "}\n");
    }
}
