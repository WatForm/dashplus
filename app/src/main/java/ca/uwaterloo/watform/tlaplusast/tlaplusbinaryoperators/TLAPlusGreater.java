package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreater extends TLAPlusInfixBinOp {

    public TLAPlusGreater(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.GREATER_THAN,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                TLAPlusOp.PrecedenceGroup.COMAPRISON);
    }
}
