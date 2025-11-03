package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusInSet extends TLAPlusBinOperatorInfix {

    public TLAPlusInSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_IN, operandOne, operandTwo);
    }
}
