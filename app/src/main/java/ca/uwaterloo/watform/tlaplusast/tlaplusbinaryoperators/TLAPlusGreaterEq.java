package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreaterEq extends TLAPlusInfixBinOp {

    public TLAPlusGreaterEq(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.GREATER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                TLAPlusOp.PrecedenceGroup.COMAPRISON);
    }
}
