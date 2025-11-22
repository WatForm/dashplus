package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusSubsetEq extends TLAPlusInfixBinaryOperator {

    public TLAPlusSubsetEq(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.SET_SUBSET_EQ,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
