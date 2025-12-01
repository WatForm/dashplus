package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubsetEq extends TLAPlusInfixBinOp {

    public TLAPlusSubsetEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_SUBSET_EQ,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
