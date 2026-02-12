package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaRange extends TlaInfixBinOp {

    /*
    a..b

    returns the set of all numbers in the range a to b
    a and b can be expressions that evaluate to integers
    */

    public TlaRange(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.RANGE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.RANGE);
    }
}
