package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreater extends TLAPlusBinOperatorInfix {

    public TLAPlusGreater(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.GREATER_THAN, operandOne, operandTwo);
    }
}
