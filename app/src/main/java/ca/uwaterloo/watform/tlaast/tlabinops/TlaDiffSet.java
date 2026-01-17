package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaDiffSet extends TlaInfixBinOp {

    public TlaDiffSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_DIFFERENCE,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.SET_OPERATORS);
    }
}
