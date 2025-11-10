package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusOr extends TLAPlusBinOperatorInfix {

    public TLAPlusOr(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.OR, operandOne, operandTwo);
    }
}
