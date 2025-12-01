package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaUnionSet extends TlaInfixBinOp {

    public TlaUnionSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_UNION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_UNION);
    }
}
