package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaOr extends TlaInfixBinOp {

    public TlaOr(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.OR,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.OR);
    }
}
