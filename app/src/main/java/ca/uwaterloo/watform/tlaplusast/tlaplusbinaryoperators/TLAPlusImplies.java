package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusImplies extends TLAPlusBinOperatorInfix {

    public TLAPlusImplies(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.IMPLICATION, operandOne, operandTwo);
    }
}
