package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNotInSet extends TLAPlusInfixBinOp {

    public TLAPlusNotInSet(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_NOT_IN,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
