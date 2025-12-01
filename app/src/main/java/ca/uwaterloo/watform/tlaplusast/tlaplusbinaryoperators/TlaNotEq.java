package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaNotEq extends TlaInfixBinOp {

    public TlaNotEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.NOT_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
