package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAdd extends TLAPlusInfixBinaryOperator {

    public TLAPlusAdd(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.PLUS,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.IRRELEVANT,
                TLAPlusOperator.PrecedenceGroup.ADD_SUB);
    }
}
