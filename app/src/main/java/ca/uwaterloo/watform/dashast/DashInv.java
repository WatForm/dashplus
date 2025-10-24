package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public class DashInv extends DashExpr {

    String name;

    public DashInv(Pos p, AlloyExpr inv) {
        super(p, inv);
    }

    public DashInv(Pos p, String n, AlloyExpr inv) {
        super(p, inv);
        assert (n != "");
        assert (n != null);
        this.name = n;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.invName, sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
