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

    // NADTODO we should print the name if it has one
    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(indent(indent) + invName);
        if (!this.name.isEmpty()) {
            sb.append(SPACE + this.name);
        }
        sb.append(" {\n");
        sb.append(indent(indent + 1));
        this.exp.toString(sb, indent + 1);
        sb.append("\n" + indent(indent) + "}\n");
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
