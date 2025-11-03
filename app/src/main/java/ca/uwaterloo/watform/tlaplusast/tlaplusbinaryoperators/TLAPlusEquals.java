package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusEquals(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.EQUALS, operandOne, operandTwo);
    }
}
