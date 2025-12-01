package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusDiffSet extends TLAPlusInfixBinOp {

    public TLAPlusDiffSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_DIFFERENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.SET_DIFFERENCE);
    }
}
