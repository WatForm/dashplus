package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAnd extends TLAPlusInfixBinOp {

    public TLAPlusAnd(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.AND,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                TLAPlusOp.PrecedenceGroup.AND);
    }
}
