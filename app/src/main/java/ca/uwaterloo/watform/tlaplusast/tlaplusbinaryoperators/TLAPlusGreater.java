package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusGreater extends TLAPlusBinOperatorInfix {

    public TLAPlusGreater(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.GREATER_THAN, operandOne, operandTwo);
    }
}
