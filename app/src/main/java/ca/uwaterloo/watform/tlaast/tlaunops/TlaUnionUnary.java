package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaUnionUnary extends TlaUnaryOp {
    public TlaUnionUnary(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.UNSAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_UNION_UNARY
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.operand);
    }
}
