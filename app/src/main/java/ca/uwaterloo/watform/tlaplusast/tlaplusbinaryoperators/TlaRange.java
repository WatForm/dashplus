package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

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
