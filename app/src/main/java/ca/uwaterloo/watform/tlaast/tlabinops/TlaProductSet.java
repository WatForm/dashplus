package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaProductSet extends TlaInfixBinOp {

    public TlaProductSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_PRODUCT,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_OPERATORS);
    }
}
