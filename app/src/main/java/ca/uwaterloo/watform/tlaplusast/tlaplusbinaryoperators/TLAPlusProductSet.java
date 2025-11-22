package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusProductSet extends TLAPlusInfixBinaryOperator {

    public TLAPlusProductSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.SET_PRODUCT,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_PRODUCT);
    }
}
