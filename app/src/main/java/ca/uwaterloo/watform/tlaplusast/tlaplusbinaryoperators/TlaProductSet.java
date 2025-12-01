package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TlaProductSet extends TlaInfixBinOp {

    public TlaProductSet(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.SET_PRODUCT,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                PrecedenceGroup.SET_PRODUCT);
    }
}
