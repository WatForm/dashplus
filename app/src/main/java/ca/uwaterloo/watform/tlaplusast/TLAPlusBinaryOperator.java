package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;

public abstract class TLAPlusBinaryOperator extends TLAPlusExpression {
    public TLAPlusBinaryOperator(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        this.children = new ArrayList<>();
        this.children.add(operandOne);
        this.children.add(operandTwo);
    }

    public TLAPlusASTNode getOperandOne() {
        return this.children.get(0);
    }

    public TLAPlusASTNode getOperandTwo() {
        return this.children.get(1);
    }
}
