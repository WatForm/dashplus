package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusProductSet extends TLAPlusBinOperatorInfix {

    public TLAPlusProductSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_PRODUCT, operandOne, operandTwo);
    }
}
