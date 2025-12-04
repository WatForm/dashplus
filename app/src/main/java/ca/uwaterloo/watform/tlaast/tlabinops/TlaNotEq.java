package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

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
