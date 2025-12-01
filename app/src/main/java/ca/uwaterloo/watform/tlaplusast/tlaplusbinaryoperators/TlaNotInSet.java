package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaNotInSet extends TlaInfixBinOp {

    public TlaNotInSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_NOT_IN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
