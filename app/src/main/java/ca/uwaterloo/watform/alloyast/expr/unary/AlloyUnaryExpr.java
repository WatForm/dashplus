package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Objects;

public abstract class AlloyUnaryExpr extends AlloyExpr {
    public final AlloyExpr sub;
    public final String op;

    public AlloyUnaryExpr(Pos pos, AlloyExpr sub, String op) {
        super(pos);
        this.sub = sub;
        this.op = op;
    }

    public AlloyUnaryExpr(AlloyExpr sub, String op) {
        super();
        this.sub = sub;
        this.op = op;
    }

    public AlloyExpr getSub() {
        return sub;
    }

    public String getOp() {
        return op;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(op);
        this.sub.toString(sb, indent);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    public abstract AlloyUnaryExpr rebuild(AlloyExpr sub);

    @Override
    public int hashCode() {
        return Objects.hash(this.op, this.sub);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyUnaryExpr other = (AlloyUnaryExpr) obj;
        if (sub == null) {
            if (other.sub != null) return false;
        } else if (!sub.equals(other.sub)) return false;
        if (op == null) {
            if (other.op != null) return false;
        } else if (!op.equals(other.op)) return false;
        return true;
    }
}
