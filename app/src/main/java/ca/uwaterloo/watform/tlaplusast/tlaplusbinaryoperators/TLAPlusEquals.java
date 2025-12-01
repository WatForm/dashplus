package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquals extends TLAPlusInfixBinOp {

    public TLAPlusEquals(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
