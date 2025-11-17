package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusUnchanged extends TLAPlusUnaryOperator {
    public TLAPlusUnchanged(TLAPlusExpression operand) {
        super(operand);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        sb.append(TLAPlusStrings.UNCHANGED + TLAPlusStrings.SPACE);
        this.getOperand().toString(sb, ident);

        return;
    }
}
