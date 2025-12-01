package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusGreater extends TLAPlusInfixBinOp {

    public TLAPlusGreater(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.GREATER_THAN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
