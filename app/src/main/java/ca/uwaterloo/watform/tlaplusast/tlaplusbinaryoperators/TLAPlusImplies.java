package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusImplies extends TLAPlusInfixBinOp {

    public TLAPlusImplies(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.IMPLICATION,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.UNSAFE,
                PrecedenceGroup.IMPLICATION);
    }
}
