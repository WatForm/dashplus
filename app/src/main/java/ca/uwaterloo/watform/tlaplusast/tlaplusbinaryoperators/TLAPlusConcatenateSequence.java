package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusConcatenateSequence extends TLAPlusBinOperatorInfix {

    public TLAPlusConcatenateSequence(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.CONCATENATE, operandOne, operandTwo);
    }
}
