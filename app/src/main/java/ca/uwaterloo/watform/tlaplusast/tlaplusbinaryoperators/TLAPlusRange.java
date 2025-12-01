package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusRange extends TLAPlusInfixBinOp {

    public TLAPlusRange(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.RANGE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.RANGE);
    }
}
