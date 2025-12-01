package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusLesserEq extends TLAPlusInfixBinOp {

    public TLAPlusLesserEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.LESSER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
