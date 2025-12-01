package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaImplies extends TlaInfixBinOp {

    public TlaImplies(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.IMPLICATION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
