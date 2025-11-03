package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusDifferenceSet extends TLAPlusBinOperatorInfix {

    public TLAPlusDifferenceSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_DIFFERENCE, operandOne, operandTwo);
    }
}
