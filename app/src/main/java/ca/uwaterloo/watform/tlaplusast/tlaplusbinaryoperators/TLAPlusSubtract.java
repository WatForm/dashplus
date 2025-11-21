package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubtract extends TLAPlusInfixBinayOperator {

    public TLAPlusSubtract(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.MINUS, operandOne, operandTwo);
    }
}
