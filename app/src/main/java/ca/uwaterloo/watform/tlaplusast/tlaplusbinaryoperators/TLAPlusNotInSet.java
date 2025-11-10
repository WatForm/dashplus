package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNotInSet extends TLAPlusBinOperatorInfix {

    public TLAPlusNotInSet(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.SET_NOT_IN, operandOne, operandTwo);
    }
}
