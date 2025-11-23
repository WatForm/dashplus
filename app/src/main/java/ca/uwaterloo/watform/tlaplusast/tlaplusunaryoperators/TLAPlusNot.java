package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNot extends TLAPlusUnaryOperator {
    public TLAPlusNot(TLAPlusExpression operand) {
        super(operand, TLAPlusOperator.PrecedenceGroup.NOT);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.NOT + this.getTLASnippetOfChild(getOperand());
    }
}
