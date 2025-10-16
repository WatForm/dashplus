package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.util.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashInv extends DashExpr {

    String name;

    public DashInv(Pos p, AlloyExpr inv) {
        super(p,inv);
    }
    public DashInv(Pos p, String n, AlloyExpr inv) {
        super(p,inv);
        assert(n != "");
        assert(n != null);
        this.name = n;
    }
    public String toString(Integer i) {
        return super.toString(DashStrings.invName + " " + name, i);
    }
}