package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusDiffSet extends TLAPlusInfixBinOp {

    public TLAPlusDiffSet(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_DIFFERENCE,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.LEFT,
                TLAPlusOp.PrecedenceGroup.SET_DIFFERENCE);
    }
}
