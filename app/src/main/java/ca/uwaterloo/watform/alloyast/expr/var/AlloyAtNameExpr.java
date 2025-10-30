package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyAtNameExpr extends AlloyVarExpr {
    public final AlloyNameExpr name;

    public AlloyAtNameExpr(Pos pos, AlloyNameExpr name) {
        super(pos, AlloyStrings.AT + name.toString());
        this.name = name;
    }

    public AlloyAtNameExpr(AlloyNameExpr name) {
        super(AlloyStrings.AT + name.toString());
        this.name = name;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyAtNameExpr rebuild(String label) {
        return new AlloyAtNameExpr(this.pos, new AlloyNameExpr(label));
    }

    public AlloyAtNameExpr rebuild(AlloyNameExpr nameExpr) {
        return new AlloyAtNameExpr(this.pos, nameExpr);
    }
}
