package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;

public class DashPred extends ASTNode implements DashStateItem {

    public final AlloyExpr exp;
    public final String name; // has no meaning

    public DashPred(Pos pos, String n, AlloyExpr i) {
        super(pos);
        assert (n != null);
        assert (i != null);
        this.name = n;
        this.exp = i;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String s = new String();
        s += DashStrings.indent(indent) + AlloyStrings.PRED + " ";
        if (name != null) s += name;
        s += " {\n";
        s += DashStrings.indent(indent) + exp.toString() + "\n";
        s += DashStrings.indent(indent) + "}\n";
        sb.append(s);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(PRED + SPACE);
        if (name != null && !name.isBlank()) pCtx.append(name + SPACE);
        pCtx.append(LBRACE);
        pCtx.brkNoSpace();
        exp.ppNewBlock(pCtx);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RBRACE);
    }

    public AlloyExpr getExp() {
        return exp;
    }

    public String getName() {
        return name;
    }
}
