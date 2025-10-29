package ca.uwaterloo.watform.alloyast.expr.binary;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyBinaryExpr extends AlloyExpr {
    public final AlloyExpr left;
    public final AlloyExpr right;
    public final String op;

    public AlloyBinaryExpr(Pos pos, AlloyExpr left, AlloyExpr right, String op) {
        super(pos);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public AlloyBinaryExpr(AlloyExpr left, AlloyExpr right, String op) {
        super();
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public AlloyExpr getLeft() {
        return left;
    }

    public AlloyExpr getRight() {
        return right;
    }

    public String getOp() {
        return op;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        if (AlloyStrings.DOT == this.op) {
            this.left.toString(sb, indent);
            sb.append(op);
            this.right.toString(sb, indent);
            return;
        }
        this.left.toString(sb, indent);
        sb.append(AlloyStrings.SPACE);
        sb.append(op);
        sb.append(AlloyStrings.SPACE);
        this.right.toString(sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    public abstract AlloyBinaryExpr rebuild(Pos pos, AlloyExpr left, AlloyExpr right);
}
