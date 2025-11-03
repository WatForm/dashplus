package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIntersectionSet extends TLAPlusBinOperatorInfix {

    public TLAPlusIntersectionSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_INTERSECTION, operandOne, operandTwo);
    }
}
