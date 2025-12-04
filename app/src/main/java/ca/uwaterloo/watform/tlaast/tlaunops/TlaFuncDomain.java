package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;

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
