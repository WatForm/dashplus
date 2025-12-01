package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaSubtract extends TlaInfixBinOp {

    public TlaSubtract(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.MINUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                PrecedenceGroup.ADD_SUB);
    }
}
