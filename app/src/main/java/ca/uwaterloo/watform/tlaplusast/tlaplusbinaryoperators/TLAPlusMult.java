package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusMult extends TLAPlusInfixBinOp {

    public TLAPlusMult(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.TIMES,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.MULT);
    }
}
