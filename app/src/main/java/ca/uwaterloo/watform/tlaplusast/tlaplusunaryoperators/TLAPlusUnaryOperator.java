package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

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
}
