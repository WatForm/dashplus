package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAdd extends TLAPlusInfixBinayOperator {

    public TLAPlusAdd(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.PLUS, operandOne, operandTwo);
    }
}
