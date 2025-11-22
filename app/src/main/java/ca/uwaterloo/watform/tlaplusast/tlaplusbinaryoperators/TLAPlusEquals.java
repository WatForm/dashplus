package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquals extends TLAPlusInfixBinaryOperator {

    public TLAPlusEquals(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.EQUALS,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.LEFT,
                TLAPlusOperator.PrecedenceGroup.COMAPRISON);
    }
}
