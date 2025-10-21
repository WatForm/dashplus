package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;

public abstract class TLAPlusUnaryOperator extends TLAPlusExpression {
    public TLAPlusUnaryOperator(TLAPlusASTNode operand) {
        this.children = new ArrayList<>();
        this.children.add(operand);
    }

    public TLAPlusASTNode getOperand() {
        return this.children.get(0);
    }
}
