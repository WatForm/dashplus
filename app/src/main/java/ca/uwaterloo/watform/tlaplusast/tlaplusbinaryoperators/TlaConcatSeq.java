package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaConcatSeq extends TlaInfixBinOp {

    public TlaConcatSeq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.CONCATENATE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.CONCAT);
    }
}
