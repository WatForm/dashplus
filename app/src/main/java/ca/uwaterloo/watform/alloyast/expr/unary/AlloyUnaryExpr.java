package ca.uwaterloo.watform.alloyast.expr.unary;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;

public class AlloyUnaryExpr extends AlloyExpr {
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
}
