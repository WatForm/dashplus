package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusLesser extends TLAPlusInfixBinayOperator {

    public TLAPlusLesser(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(TLAPlusStrings.LESSER_THAN, operandOne, operandTwo);
    }
}
