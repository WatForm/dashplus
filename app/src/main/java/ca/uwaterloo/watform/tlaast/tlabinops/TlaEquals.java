package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaEquals extends TlaInfixBinOp {

    /*
    exp1 = exp2

    equality of two expressions
    */

    public TlaEquals(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMPARISON);
    }
}
