package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubsetEq extends TLAPlusInfixBinOp {

    public TLAPlusSubsetEq(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_SUBSET_EQ,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
