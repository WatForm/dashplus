package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusLesserEq extends TLAPlusInfixBinOp {

    public TLAPlusLesserEq(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.LESSER_THAN_EQUALS,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                TLAPlusOp.PrecedenceGroup.COMAPRISON);
    }
}
