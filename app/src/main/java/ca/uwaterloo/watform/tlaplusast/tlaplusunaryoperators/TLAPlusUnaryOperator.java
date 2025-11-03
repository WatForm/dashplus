package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;

public abstract class TLAPlusUnaryOperator extends TLAPlusExpression {
    public TLAPlusUnaryOperator(ASTNode operand) {
        this.children = new ArrayList<>();
        this.children.add(operand);
    }

    public ASTNode getOperand() {
        return this.children.get(0);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
