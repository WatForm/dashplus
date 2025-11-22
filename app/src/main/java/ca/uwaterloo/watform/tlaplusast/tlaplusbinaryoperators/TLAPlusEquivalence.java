package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquivalence extends TLAPlusInfixBinaryOperator {

    public TLAPlusEquivalence(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.EQUIVALENCE,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
