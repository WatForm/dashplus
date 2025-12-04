package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

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
