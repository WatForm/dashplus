package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquivalence extends TLAPlusInfixBinayOperator {

    public TLAPlusEquivalence(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.EQUIVALENCE, operandOne, operandTwo);
    }
}
