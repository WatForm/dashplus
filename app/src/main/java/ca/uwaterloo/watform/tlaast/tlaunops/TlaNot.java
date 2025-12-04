package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaNot extends TlaUnaryOp {
    public TlaNot(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.NOT);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.NOT + this.getTLASnippetOfChild(this.operand);
    }
}
