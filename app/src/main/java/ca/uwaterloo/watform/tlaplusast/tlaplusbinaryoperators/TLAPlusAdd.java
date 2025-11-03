package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusAdd extends TLAPlusBinOperatorInfix {

    public TLAPlusAdd(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.PLUS, operandOne, operandTwo);
    }
}
