package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreaterEq extends TLAPlusInfixBinOp {

    public TLAPlusGreaterEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.GREATER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
