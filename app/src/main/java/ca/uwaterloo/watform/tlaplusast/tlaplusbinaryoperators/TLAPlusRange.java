package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusRange extends TLAPlusInfixBinOp {

    public TLAPlusRange(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.RANGE,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.UNSAFE,
                PrecedenceGroup.RANGE);
    }
}
