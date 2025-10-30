package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyAtNameExpr extends AlloyVarExpr {
    public final AlloyQnameExpr name;

    public AlloyAtNameExpr(Pos pos, AlloyQnameExpr name) {
        super(pos, AlloyStrings.AT + name.toString());
        this.name = name;
    }

    public AlloyAtNameExpr(AlloyQnameExpr name) {
        super(AlloyStrings.AT + name.toString());
        this.name = name;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyAtNameExpr rebuild(String label) {
        return new AlloyAtNameExpr(this.pos, new AlloyQnameExpr(label));
    }

    public AlloyAtNameExpr rebuild(AlloyQnameExpr nameExpr) {
        return new AlloyAtNameExpr(this.pos, nameExpr);
    }
}
