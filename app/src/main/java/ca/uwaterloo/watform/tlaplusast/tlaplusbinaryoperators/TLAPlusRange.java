package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusRange extends TLAPlusInfixBinaryOperator {

    public TLAPlusRange(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.RANGE,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.UNSAFE,
                PrecedenceGroup.RANGE);
    }
}
