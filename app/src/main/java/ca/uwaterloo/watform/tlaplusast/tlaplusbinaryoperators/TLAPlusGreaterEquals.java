package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreaterEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusGreaterEquals(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.GREATER_THAN_EQUALS, operandOne, operandTwo);
    }
}
