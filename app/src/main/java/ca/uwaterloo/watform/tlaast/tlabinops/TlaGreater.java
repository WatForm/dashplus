package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaGreater extends TlaInfixBinOp {

    /*
    exp1 > exp2
    */

    public TlaGreater(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.GREATER_THAN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMPARISON);
    }
}
