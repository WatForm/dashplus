package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaGreaterEq extends TlaInfixBinOp {

    public TlaGreaterEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.GREATER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
