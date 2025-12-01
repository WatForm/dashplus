package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAdd extends TLAPlusInfixBinOp {

    public TLAPlusAdd(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.PLUS,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                TLAPlusOp.PrecedenceGroup.ADD_SUB);
    }
}
