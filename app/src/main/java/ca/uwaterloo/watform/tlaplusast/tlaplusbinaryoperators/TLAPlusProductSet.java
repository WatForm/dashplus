package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusProductSet extends TLAPlusBinOperatorInfix {

    public TLAPlusProductSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_PRODUCT, operandOne, operandTwo);
    }
}
