package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubtract extends TLAPlusInfixBinaryOperator {

    public TLAPlusSubtract(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.MINUS,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.LEFT,
                PrecedenceGroup.ADD_SUB);
    }
}
