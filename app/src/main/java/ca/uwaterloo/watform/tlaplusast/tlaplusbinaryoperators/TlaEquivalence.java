package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaEquivalence extends TlaInfixBinOp {

    public TlaEquivalence(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUIVALENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
