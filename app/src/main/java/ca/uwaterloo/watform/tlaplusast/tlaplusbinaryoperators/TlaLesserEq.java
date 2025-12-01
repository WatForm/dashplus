package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaLesserEq extends TlaInfixBinOp {

    public TlaLesserEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.LESSER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
