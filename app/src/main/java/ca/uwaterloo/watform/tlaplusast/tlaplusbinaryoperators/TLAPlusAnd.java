package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusAnd extends TLAPlusBinOperatorInfix {

    public TLAPlusAnd(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.AND, operandOne, operandTwo);
    }
}
