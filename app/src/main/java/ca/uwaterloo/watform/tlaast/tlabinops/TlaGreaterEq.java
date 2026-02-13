package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaGreaterEq extends TlaInfixBinOp {

    /*
    exp1 >= exp2
    */

    public TlaGreaterEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.GREATER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMPARISON);
    }
}
