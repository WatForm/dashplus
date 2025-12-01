package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusConcatSeq extends TLAPlusInfixBinOp {

    public TLAPlusConcatSeq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.CONCATENATE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.CONCAT);
    }
}
