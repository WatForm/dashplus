package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaAdd extends TlaInfixBinOp {

    public TlaAdd(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.PLUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.ADD_SUB);
    }
}
