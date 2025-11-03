package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusGreaterEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusGreaterEquals(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.GREATER_THAN_EQUALS, operandOne, operandTwo);
    }
}
