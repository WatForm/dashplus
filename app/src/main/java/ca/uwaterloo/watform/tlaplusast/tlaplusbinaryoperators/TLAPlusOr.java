package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusOr extends TLAPlusInfixBinayOperator {

    public TLAPlusOr(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.OR, operandOne, operandTwo);
    }
}
