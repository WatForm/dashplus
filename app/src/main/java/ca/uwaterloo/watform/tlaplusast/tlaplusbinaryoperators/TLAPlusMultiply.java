package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusMultiply extends TLAPlusInfixBinayOperator {

    public TLAPlusMultiply(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.TIMES, operandOne, operandTwo);
    }
}
