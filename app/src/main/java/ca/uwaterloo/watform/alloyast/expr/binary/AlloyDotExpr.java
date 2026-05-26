/*
	Relational Join (Dot) Operator Usage:

	1. Field Access:
	sig Name {}
	sig Person { name: Name }
	fact { all p: Person | some p.name }

	2. Function/Predicate Application:
	sig A {}
	pred p[a: A] {
		some a
	}
	fact {
		some a: A | a.p
	}

	3. Relational Join:
	sig Country {}

	sig City {
		locatedIn: Country
	}

	sig Person {
		livesIn: City
	}

	pred show {
		some livesIn . locatedIn
	}

	run show for 3
*/

package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public final class AlloyDotExpr extends AlloyBinaryExpr {
    public AlloyDotExpr(Pos pos, AlloyExpr left, AlloyExpr right) {
        super(pos, left, right, AlloyStrings.DOT);
    }

    public AlloyDotExpr(AlloyExpr left, AlloyExpr right) {
        super(left, right, AlloyStrings.DOT);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyDotExpr rebuild(AlloyExpr left, AlloyExpr right) {
        return new AlloyDotExpr(this.pos, left, right);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        this.left.toString(sb, indent);
        sb.append(op);
        this.right.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.appendChild(this, this.left, false);
        pCtx.append(op);
        pCtx.brkNoSpace();
        pCtx.appendChild(this, this.right, false);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.DOT_PREC;
    }
}
