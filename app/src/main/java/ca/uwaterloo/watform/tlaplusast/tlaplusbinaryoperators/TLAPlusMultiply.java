package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusMultiply extends TLAPlusBinOperatorInfix {

    public TLAPlusMultiply(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.TIMES, operandOne, operandTwo);
    }
}
