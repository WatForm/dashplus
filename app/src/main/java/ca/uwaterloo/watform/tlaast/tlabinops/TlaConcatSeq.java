package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

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
