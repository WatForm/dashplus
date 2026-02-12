package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaSubtract extends TlaInfixBinOp {

    /*
    exp1 - exp2

    subtraction
    */

    public TlaSubtract(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.MINUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                PrecedenceGroup.ADD_SUB);
    }
}
