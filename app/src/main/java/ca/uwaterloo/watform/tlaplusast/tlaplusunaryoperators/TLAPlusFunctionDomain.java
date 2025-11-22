package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public class TLAPlusFunctionDomain extends TLAPlusUnaryOperator {

    public TLAPlusFunctionDomain(TLAPlusExpression operand) {
        super(operand, TLAPlusOperator.PrecedenceGroup.UNSAFE);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        sb.append(TLAPlusStrings.DOMAIN + TLAPlusStrings.SPACE);
        this.getOperand().toString(sb, ident);

        return;
    }
}
