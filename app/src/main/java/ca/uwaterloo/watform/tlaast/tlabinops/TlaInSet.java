package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaInSet extends TlaInfixBinOp {

    /*
    exp \in S

    set membership
    */

    public TlaInSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_IN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.SET_MEMBERSHIP);
    }
}
