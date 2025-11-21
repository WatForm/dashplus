package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAnd extends TLAPlusInfixBinayOperator {

    public TLAPlusAnd(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.AND, operandOne, operandTwo);
    }
}
