package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaSubsetUnary extends TlaUnaryOp {
    public TlaSubsetUnary(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.SET_OPERATORS);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_SUBSET_UNARY
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.operand);
    }
}
