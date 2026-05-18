/*

	Syntax Example:
	sig A, B{}
	sig C {
		f: A -> B -> B
	}
	pred p[a: A -> A] {
		...
	}
	fact {
		C.f in A -> B -> B
	}
*/

package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.*;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public final class AlloyArrowExpr extends AlloyBinaryExpr {
    public final Optional<AlloyQtEnum> mul1;
    public final Optional<AlloyQtEnum> mul2;

    public AlloyArrowExpr(
            Pos pos, AlloyExpr left, AlloyQtEnum mul1, AlloyQtEnum mul2, AlloyExpr right) {
        super(
                pos,
                left,
                right,
                (mul1 == null ? "" : mul1.toString())
                        + RARROW
                        + (mul2 == null ? "" : mul2.toString()));
        this.mul1 = Optional.ofNullable(mul1);
        this.mul2 = Optional.ofNullable(mul2);
        reqNonNull(nullField(pos, this));
        if (!this.mul1.isEmpty() && !AlloyQtEnum.MUL.contains(this.mul1.get())
                || !this.mul2.isEmpty() && !AlloyQtEnum.MUL.contains(this.mul2.get())) {
            throw AlloyCtorError.invalidAlloyQtEnum(
                    pos,
                    this.getClass().getSimpleName() + ".mul must be LONE, ONE, SOME, or SET. ");
        }
    }

    public AlloyArrowExpr(AlloyExpr left, AlloyQtEnum mul1, AlloyQtEnum mul2, AlloyExpr right) {
        this(Pos.UNKNOWN, left, mul1, mul2, right);
    }

    public AlloyArrowExpr(AlloyExpr left, AlloyExpr right) {
        this(Pos.UNKNOWN, left, null, null, right);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyArrowExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyArrowExpr(
                this.pos, left, this.mul1.orElse(null), this.mul2.orElse(null), right);
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
