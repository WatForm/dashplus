package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNot extends TLAPlusUnaryOperator {
    public TLAPlusNot(TLAPlusExpression operand) {
        super(operand);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        sb.append(TLAPlusStrings.NOT + TLAPlusStrings.SPACE);
        this.getOperand().toString(sb, ident);

        return;
    }
}
