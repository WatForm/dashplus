package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

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
