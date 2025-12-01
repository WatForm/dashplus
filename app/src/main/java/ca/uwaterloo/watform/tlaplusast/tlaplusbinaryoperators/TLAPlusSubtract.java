package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubtract extends TLAPlusInfixBinOp {

    public TLAPlusSubtract(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.MINUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                PrecedenceGroup.ADD_SUB);
    }
}
