package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusConcatSeq extends TLAPlusInfixBinOp {

    public TLAPlusConcatSeq(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.CONCATENATE,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                TLAPlusOp.PrecedenceGroup.CONCAT);
    }
}
