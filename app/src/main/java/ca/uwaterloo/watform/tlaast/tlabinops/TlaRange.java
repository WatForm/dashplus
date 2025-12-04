package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaRange extends TlaInfixBinOp {

    public TlaRange(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.RANGE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.RANGE);
    }
}
