package ca.uwaterloo.watform.dashast.dashNamedExpr;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStateItem;
import ca.uwaterloo.watform.dashast.DashStrings;
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

    // NADTODO we should print the name if it has one
    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(DashStrings.indent(indent) + DashStrings.invName);
        if (!this.name.isEmpty()) {
            sb.append(CommonStrings.SPACE + this.name);
        }
        sb.append(" {\n");
        sb.append(DashStrings.indent(indent + 1));
        this.exp.toString(sb, indent + 1);
        sb.append("\n" + DashStrings.indent(indent) + "}\n");
    }
}
