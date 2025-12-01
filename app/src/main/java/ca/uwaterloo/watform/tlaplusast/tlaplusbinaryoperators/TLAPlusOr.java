package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusOr extends TLAPlusInfixBinOp {

    public TLAPlusOr(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.OR,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                PrecedenceGroup.OR);
    }
}
