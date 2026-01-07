package ca.uwaterloo.watform.alloyast.expr;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyExpr extends AlloyASTNode {
    public AlloyExpr(Pos pos) {
        super(pos);
    }

    public AlloyExpr() {
        super();
    }

    public abstract <T> T accept(AlloyExprVis<T> visitor);
}
