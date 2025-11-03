package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusNotEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusNotEquals(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.NOT_EQUALS, operandOne, operandTwo);
    }
}
