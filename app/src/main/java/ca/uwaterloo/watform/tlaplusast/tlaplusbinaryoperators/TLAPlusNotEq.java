package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNotEq extends TLAPlusInfixBinOp {

    public TLAPlusNotEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.NOT_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
