package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaEquivalence extends TlaInfixBinOp {

    /*
    exp1 <=> exp2

    if and only if
    bi-directional implication
    */

    public TlaEquivalence(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.EQUIVALENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
