package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusEquals(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.EQUALS, operandOne, operandTwo);
    }
}
