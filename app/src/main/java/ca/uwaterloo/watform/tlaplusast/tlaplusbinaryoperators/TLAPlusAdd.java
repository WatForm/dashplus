package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusAdd extends TLAPlusInfixBinOp {

    public TLAPlusAdd(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.PLUS,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.ADD_SUB);
    }
}
