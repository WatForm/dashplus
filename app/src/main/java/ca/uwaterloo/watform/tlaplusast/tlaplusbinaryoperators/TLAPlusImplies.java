package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusImplies extends TLAPlusBinOperatorInfix {

    public TLAPlusImplies(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.IMPLICATION, operandOne, operandTwo);
    }
}
