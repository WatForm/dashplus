package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusInSet extends TLAPlusInfixBinOp {

    public TLAPlusInSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_IN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
