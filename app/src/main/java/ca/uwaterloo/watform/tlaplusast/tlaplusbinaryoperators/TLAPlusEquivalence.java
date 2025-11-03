package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusEquivalence extends TLAPlusBinOperatorInfix {

    public TLAPlusEquivalence(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.EQUIVALENCE, operandOne, operandTwo);
    }
}
