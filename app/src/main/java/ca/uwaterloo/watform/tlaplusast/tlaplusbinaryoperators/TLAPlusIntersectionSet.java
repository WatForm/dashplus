package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIntersectionSet extends TLAPlusInfixBinayOperator {

    public TLAPlusIntersectionSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_INTERSECTION, operandOne, operandTwo);
    }
}
