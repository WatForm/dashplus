package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusIntersectionSet extends TLAPlusBinOperatorInfix {

    public TLAPlusIntersectionSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_INTERSECTION, operandOne, operandTwo);
    }
}
