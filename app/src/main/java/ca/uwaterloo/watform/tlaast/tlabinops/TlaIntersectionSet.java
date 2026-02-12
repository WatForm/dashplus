package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaIntersectionSet extends TlaInfixBinOp {

    /*
    S1 \intersect S2

    set intersection
    */

    public TlaIntersectionSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_INTERSECTION,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_OPERATORS);
    }
}
