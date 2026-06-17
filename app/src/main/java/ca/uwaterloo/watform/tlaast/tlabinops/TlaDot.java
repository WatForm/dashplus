package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;

public class TlaDot extends TlaInfixBinOp {

    /*
       f.x

    f is a record
       */

    public TlaDot(TlaExp operandOne, TlaExp operandTwo) {
        super(
                TlaStrings.DOT,
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.UNSAFE);
    }
}
