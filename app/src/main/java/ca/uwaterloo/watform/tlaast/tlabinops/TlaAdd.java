package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaAdd extends TlaInfixBinOp {

    /*
    exp1 + exp2

    addition
    */

    public TlaAdd(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.PLUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.ADD_SUB);
    }
}
