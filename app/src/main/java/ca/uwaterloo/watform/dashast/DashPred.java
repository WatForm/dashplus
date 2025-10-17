package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.AlloyStrings;

public class DashPred extends ASTNode {

    public AlloyExpr exp;
    public String name; // has no meaning
 
    public DashPred(Pos pos, String n, AlloyExpr i) {
        super(pos);
        assert(n != null);
        assert(i != null);
        this.name = n;
        this.exp = i;
    }
    @Override
    public void toString(StringBuilder sb, int indent) {
        String s = new String();
        s += DashStrings.indent(indent) + AlloyStrings.PRED +" "; 
        if (name != null) s += name;
        s += " {\n";
        s += DashStrings.indent(indent) + exp.toString() + "\n";
        s += DashStrings.indent(indent) + "}\n";
        sb.append(s);
    }
    public AlloyExpr getExp() {
        return exp;
    }
    public String getName() {
        return name;
    }
}
