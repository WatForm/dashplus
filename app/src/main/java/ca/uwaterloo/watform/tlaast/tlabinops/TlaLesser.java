package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaLesser extends TlaInfixBinOp {

    public TlaLesser(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.LESSER_THAN,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.LEFT,
                TlaOperator.PrecedenceGroup.COMAPRISON);
    }
}
