package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusInSet extends TLAPlusInfixBinOp {

    public TLAPlusInSet(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_IN,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
