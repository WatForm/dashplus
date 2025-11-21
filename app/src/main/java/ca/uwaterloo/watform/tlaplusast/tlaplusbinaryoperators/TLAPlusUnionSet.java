package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusUnionSet extends TLAPlusInfixBinayOperator {

    public TLAPlusUnionSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_UNION, operandOne, operandTwo);
    }
}
