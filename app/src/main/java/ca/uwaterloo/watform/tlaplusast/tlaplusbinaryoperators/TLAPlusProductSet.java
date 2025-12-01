package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusProductSet extends TLAPlusInfixBinOp {

    public TLAPlusProductSet(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_PRODUCT,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_PRODUCT);
    }
}
