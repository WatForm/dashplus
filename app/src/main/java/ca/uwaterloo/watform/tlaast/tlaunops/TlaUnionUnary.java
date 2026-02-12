package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaUnionUnary extends TlaUnaryOp {

    /*
    UNION {exp1,exp2...}

    syntactic sugar for exp1 \\union exp2 \\union ... in TLA+

    */

    public TlaUnionUnary(TlaExp operand) {
        super(operand, TlaOperator.PrecedenceGroup.SET_OPERATORS);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_UNION_UNARY
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.operand);
    }
}
