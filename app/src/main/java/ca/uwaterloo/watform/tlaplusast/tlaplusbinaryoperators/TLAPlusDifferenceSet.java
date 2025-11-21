package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusDifferenceSet extends TLAPlusInfixBinayOperator {

    public TLAPlusDifferenceSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_DIFFERENCE, operandOne, operandTwo);
    }
}
