package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusConcatenateSequence extends TLAPlusBinOperatorInfix {

    public TLAPlusConcatenateSequence(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.CONCATENATE, operandOne, operandTwo);
    }
}
