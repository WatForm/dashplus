package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNotEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusNotEquals(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.NOT_EQUALS, operandOne, operandTwo);
    }
}
