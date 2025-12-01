package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaEquals extends TlaInfixBinOp {

    public TlaEquals(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
