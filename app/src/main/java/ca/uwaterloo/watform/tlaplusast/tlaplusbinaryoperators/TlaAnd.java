package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaAnd extends TlaInfixBinOp {

    public TlaAnd(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.AND,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.AND);
    }
}
