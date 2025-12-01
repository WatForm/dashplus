package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaNot extends TlaUnaryOp {
    public TlaNot(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.NOT);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.NOT + this.getTLASnippetOfChild(this.operand);
    }
}
