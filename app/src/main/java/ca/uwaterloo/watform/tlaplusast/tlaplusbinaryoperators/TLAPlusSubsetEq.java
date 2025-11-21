package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubsetEq extends TLAPlusInfixBinayOperator {

    public TLAPlusSubsetEq(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_SUBSET_EQ, operandOne, operandTwo);
    }
}
