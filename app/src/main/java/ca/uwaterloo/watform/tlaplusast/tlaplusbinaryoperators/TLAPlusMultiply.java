package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusMultiply extends TLAPlusInfixBinaryOperator {

    public TLAPlusMultiply(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.TIMES,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.MULT);
    }
}
