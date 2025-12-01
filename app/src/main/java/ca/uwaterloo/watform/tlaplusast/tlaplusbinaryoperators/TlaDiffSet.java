package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaDiffSet extends TlaInfixBinOp {

    public TlaDiffSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_DIFFERENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.SET_DIFFERENCE);
    }
}
