package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusMult extends TLAPlusInfixBinOp {

    public TLAPlusMult(TLAPlusExp operandOne, TLAPlusExp operandTwo) {
        super(
                TLAPlusStrings.TIMES,
                operandOne,
                operandTwo,
                TLAPlusOp.Associativity.IRRELEVANT,
                PrecedenceGroup.MULT);
    }
}
