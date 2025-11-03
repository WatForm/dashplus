package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusSubsetEq extends TLAPlusBinOperatorInfix {

    public TLAPlusSubsetEq(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_SUBSET_EQ, operandOne, operandTwo);
    }
}
