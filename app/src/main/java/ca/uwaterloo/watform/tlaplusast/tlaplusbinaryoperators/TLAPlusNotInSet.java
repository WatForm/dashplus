package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusNotInSet extends TLAPlusBinOperatorInfix {

    public TLAPlusNotInSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_NOT_IN, operandOne, operandTwo);
    }
}
