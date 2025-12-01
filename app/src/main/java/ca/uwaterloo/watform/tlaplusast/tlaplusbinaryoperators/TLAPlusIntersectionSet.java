package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIntersectionSet extends TLAPlusInfixBinOp {

    public TLAPlusIntersectionSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_INTERSECTION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_INTERSECTION);
    }
}
