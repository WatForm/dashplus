/*
    defaults:
        in A m->n B
        if either m or n are not explicitly given, they are replaced  
        by multiplicity SET during parsing
*/

package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.*;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyASTImplError;
import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyArrowExpr extends AlloyBinaryExpr {
    public final AlloyQtEnum mul1;
    public final AlloyQtEnum mul2;

    public AlloyArrowExpr(
            Pos pos, AlloyExpr left, AlloyQtEnum mul1, AlloyQtEnum mul2, AlloyExpr right) {
        super(pos, left, right, (mul1.toString()) + RARROW + (mul2.toString()));
        this.mul1 = mul1;
        this.mul2 = mul2;
        reqNonNull(nullField(pos, this), this.mul1, this.mul2);
        if (!AlloyQtEnum.MUL.contains(this.mul1) || !AlloyQtEnum.MUL.contains(this.mul2)) {
            throw AlloyASTImplError.invalidAlloyQtEnum(
                    pos,
                    this.getClass().getSimpleName() + ".mul must be LONE, ONE, SOME, or SET. ");
        }
    }

    public AlloyArrowExpr(AlloyExpr left, AlloyQtEnum mul1, AlloyQtEnum mul2, AlloyExpr right) {
        this(Pos.UNKNOWN, left, mul1, mul2, right);
    }

    public AlloyArrowExpr(AlloyExpr left, AlloyExpr right) {
        this(Pos.UNKNOWN, left, AlloyQtEnum.SET, AlloyQtEnum.SET, right);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyArrowExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyArrowExpr(this.pos, left, this.mul1, this.mul2, right);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.ARROW_PREC;
    }

    @Override
    public boolean isLeftAssoc() {
        return false;
    }
}
