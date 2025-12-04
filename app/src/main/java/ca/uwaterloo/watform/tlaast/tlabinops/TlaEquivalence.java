package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

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
