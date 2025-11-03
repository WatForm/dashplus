package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusOr extends TLAPlusBinOperatorInfix {

    public TLAPlusOr(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.OR, operandOne, operandTwo);
    }
}
