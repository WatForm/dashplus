package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusConcatenateSequence extends TLAPlusInfixBinayOperator {

    public TLAPlusConcatenateSequence(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.CONCATENATE, operandOne, operandTwo);
    }
}
