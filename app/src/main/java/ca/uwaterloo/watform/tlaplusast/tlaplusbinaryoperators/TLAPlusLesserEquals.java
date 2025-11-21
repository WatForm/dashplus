package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusLesserEquals extends TLAPlusInfixBinaryOperator {

    public TLAPlusLesserEquals(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.LESSER_THAN_EQUALS, operandOne, operandTwo);
    }
}
