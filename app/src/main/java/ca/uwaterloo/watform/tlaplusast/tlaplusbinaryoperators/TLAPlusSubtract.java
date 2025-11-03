package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusSubtract extends TLAPlusBinOperatorInfix {

    public TLAPlusSubtract(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.MINUS, operandOne, operandTwo);
    }
}
