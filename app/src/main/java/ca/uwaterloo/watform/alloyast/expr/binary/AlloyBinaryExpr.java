package ca.uwaterloo.watform.alloyast.expr.binary;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Objects;

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

    public abstract AlloyBinaryExpr rebuild(AlloyExpr left, AlloyExpr right);

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.op, this.right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyBinaryExpr other = (AlloyBinaryExpr) obj;
        if (left == null) {
            if (other.left != null) return false;
        } else if (!left.equals(other.left)) return false;
        if (right == null) {
            if (other.right != null) return false;
        } else if (!right.equals(other.right)) return false;
        if (op == null) {
            if (other.op != null) return false;
        } else if (!op.equals(other.op)) return false;
        return true;
    }

    @Override
    public void pp(PrintContext pCtx) {
        this.left.ppNewBlock(pCtx);
        pCtx.append(SPACE + this.op);
        pCtx.brk();
        this.right.ppNewBlock(pCtx);
    }
}
