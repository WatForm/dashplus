package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusInSet extends TLAPlusInfixBinayOperator {

    public TLAPlusInSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_IN, operandOne, operandTwo);
    }
}
