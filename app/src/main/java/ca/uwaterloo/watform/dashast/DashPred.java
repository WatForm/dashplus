package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashPred extends Dash {

    public AlloyExpr exp;
    public String name; // has no meaning
 
    public DashPred(Pos p, String n, AlloyExpr i) {
        assert(n != null);
        assert(i != null);
        this.pos = pos;
        this.name = n;
        this.exp = i;
    }
    public String toString(Integer i) {
        String s = new String();
        s += DashStrings.indent(i) + DashStrings.predName +" "; 
        if (name != null) s += name;
        s += " {\n";
        s += DashStrings.indent(i) + exp.toString() + "\n";
        s += DashStrings.indent(i) + "}\n";
        return s;
    }
    public AlloyExpr getExp() {
        return exp;
    }
    public String getName() {
        return name;
    }
}