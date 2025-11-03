package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusUnionSet extends TLAPlusBinOperatorInfix {

    public TLAPlusUnionSet(ASTNode operandOne, ASTNode operandTwo) {
        super(TLAPlusStrings.SET_UNION, operandOne, operandTwo);
    }
}
