package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVar;

public class TLAPlusPrime extends TLAPlusUnaryOp {
    public TLAPlusPrime(TLAPlusVar operand) {
        super(operand, TLAPlusOp.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operand) + TLAPlusStrings.PRIME;
    }
}
