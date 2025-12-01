package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;

public class TlaFuncDomain extends TlaUnaryOp {

    public TlaFuncDomain(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.UNSAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.DOMAIN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.operand)
                + TlaStrings.PRIME;
    }
}
