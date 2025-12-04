package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class TlaPrime extends TlaUnaryOp {
    public TlaPrime(TlaVar operand) {
        super(operand, TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operand) + TlaStrings.PRIME;
    }
}
