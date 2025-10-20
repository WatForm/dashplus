package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
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
}
