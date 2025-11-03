package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusLesserEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusLesserEquals(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.LESSER_THAN_EQUALS, operandOne, operandTwo);
    }
}
