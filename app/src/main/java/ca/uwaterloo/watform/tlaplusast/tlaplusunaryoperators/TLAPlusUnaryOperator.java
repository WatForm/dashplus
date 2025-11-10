package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusUnaryOperator extends TLAPlusExpression {

    private TLAPlusExpression operand;

    public TLAPlusUnaryOperator(TLAPlusExpression operand) {
        this.operand = operand;
    }

    public TLAPlusExpression getOperand() {
        return this.operand;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.operand);
        return children;
    }
}
