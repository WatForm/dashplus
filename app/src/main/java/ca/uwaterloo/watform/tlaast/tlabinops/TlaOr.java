package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaOr extends TlaInfixBinOp {

    /*

    exp1 \/ exp2
    */

    public TlaOr(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.OR,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.AND_OR);
    }
}
