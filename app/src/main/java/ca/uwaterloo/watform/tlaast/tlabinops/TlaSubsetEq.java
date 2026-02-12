package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaSubsetEq extends TlaInfixBinOp {

    /*
    S1 \subseteq S2

    S1 is a subset of S2  (not proper subset, those do not have operators and need to be constructed)
    */

    public TlaSubsetEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_SUBSET_EQ,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
