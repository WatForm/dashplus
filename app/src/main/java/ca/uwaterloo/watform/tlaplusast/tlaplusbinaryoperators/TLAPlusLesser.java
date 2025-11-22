package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusLesser extends TLAPlusInfixBinaryOperator {

    public TLAPlusLesser(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                TLAPlusStrings.LESSER_THAN,
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.LEFT,
                TLAPlusOperator.PrecedenceGroup.COMAPRISON);
    }
}
