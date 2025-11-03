package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusLesser extends TLAPlusBinOperatorInfix {

    public TLAPlusLesser(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.LESSER_THAN, operandOne, operandTwo);
    }
}
