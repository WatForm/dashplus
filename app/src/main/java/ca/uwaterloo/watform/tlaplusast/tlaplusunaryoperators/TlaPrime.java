package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;
import ca.uwaterloo.watform.tlaplusast.TlaVar;

public class TlaPrime extends TlaUnaryOp {
    public TlaPrime(TlaVar operand) {
        super(operand, TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operand) + TlaStrings.PRIME;
    }
}
