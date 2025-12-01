package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

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
