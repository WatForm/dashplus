package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaIntersectionSet extends TlaInfixBinOp {

    public TlaIntersectionSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_INTERSECTION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_INTERSECTION);
    }
}
