package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAnd extends TLAPlusInfixBinOp {

    public TLAPlusAnd(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.AND,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.AND);
    }
}
