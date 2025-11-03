package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusRange extends TLAPlusBinOperatorInfix {

    public TLAPlusRange(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.RANGE, operandOne, operandTwo);
    }
}
