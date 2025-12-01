package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubtract extends TLAPlusInfixBinOp {

    public TLAPlusSubtract(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.MINUS,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                PrecedenceGroup.ADD_SUB);
    }
}
