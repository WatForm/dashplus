package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaMult extends TlaInfixBinOp {

    public TlaMult(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.TIMES,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.MULT);
    }
}
