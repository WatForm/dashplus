package ca.uwaterloo.watform.alloyast.expr.var;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

/*
 * Prefixing a field name with the special symbol @ suppresses the implicit expansion in sig's facts
 * see: section on Signatures in https://alloytools.org/spec.html
 */
public final class AlloyAtNameExpr extends AlloyVarExpr {
    public final AlloyQnameExpr name;

    public AlloyAtNameExpr(Pos pos, AlloyQnameExpr name) {
        super(pos, AlloyStrings.AT + name.toString());
        this.name = name;
        reqNonNull(nullField(pos, this), this.name);
    }

    public AlloyAtNameExpr(AlloyQnameExpr name) {
        this(Pos.UNKNOWN, name);
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
