package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIntersectionSet extends TLAPlusInfixBinOp {

    public TLAPlusIntersectionSet(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.SET_INTERSECTION,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_INTERSECTION);
    }
}
