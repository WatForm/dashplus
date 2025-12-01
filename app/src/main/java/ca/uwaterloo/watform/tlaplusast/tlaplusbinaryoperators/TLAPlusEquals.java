package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquals extends TLAPlusInfixBinOp {

    public TLAPlusEquals(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.EQUALS,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                TLAPlusOp.PrecedenceGroup.COMAPRISON);
    }
}
