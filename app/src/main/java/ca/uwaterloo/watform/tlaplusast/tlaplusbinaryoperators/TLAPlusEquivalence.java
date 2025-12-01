package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusEquivalence extends TLAPlusInfixBinOp {

    public TLAPlusEquivalence(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUIVALENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
