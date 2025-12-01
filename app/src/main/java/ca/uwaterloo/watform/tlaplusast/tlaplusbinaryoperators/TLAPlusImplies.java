package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusImplies extends TLAPlusInfixBinOp {

    public TLAPlusImplies(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.IMPLICATION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
