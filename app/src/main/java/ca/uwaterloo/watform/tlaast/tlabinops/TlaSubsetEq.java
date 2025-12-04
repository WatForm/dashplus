package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaSubsetEq extends TlaInfixBinOp {

    public TlaSubsetEq(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_SUBSET_EQ,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
