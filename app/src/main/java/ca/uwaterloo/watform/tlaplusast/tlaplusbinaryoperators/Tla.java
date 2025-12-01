package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class Tla extends TlaInfixBinOp {

    public Tla(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.AND,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.AND);
    }
}
