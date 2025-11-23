package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public class TLAPlusFunctionDomain extends TLAPlusUnaryOperator {

    public TLAPlusFunctionDomain(TLAPlusExpression operand) {
        super(operand, TLAPlusOperator.PrecedenceGroup.UNSAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.DOMAIN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(getOperand())
                + TLAPlusStrings.PRIME;
    }
}
