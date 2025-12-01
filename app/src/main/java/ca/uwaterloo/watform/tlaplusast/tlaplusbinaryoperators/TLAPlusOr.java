package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusOr extends TLAPlusInfixBinOp {

    public TLAPlusOr(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.OR,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.OR);
    }
}
