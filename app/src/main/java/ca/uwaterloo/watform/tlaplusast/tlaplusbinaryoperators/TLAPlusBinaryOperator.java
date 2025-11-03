package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;

public abstract class TLAPlusBinaryOperator extends TLAPlusExpression {
    public TLAPlusBinaryOperator(ASTNode operandOne, ASTNode operandTwo) {
        this.children = new ArrayList<>();
        this.children.add(operandOne);
        this.children.add(operandTwo);
    }

    public ASTNode getOperandOne() {
        return this.children.get(0);
    }

    public ASTNode getOperandTwo() {
        return this.children.get(1);
    }

    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
