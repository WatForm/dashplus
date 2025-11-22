package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAnd extends TLAPlusInfixBinaryOperator {

    public TLAPlusAnd(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.AND,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.IRRELEVANT,
                TLAPlusOperator.PrecedenceGroup.AND);
    }
}
