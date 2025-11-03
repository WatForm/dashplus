package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public abstract class TLAPlusBinaryOperator extends TLAPlusExpression {

    private TLAPlusExpression operandOne;
    private TLAPlusExpression operandTwo;

    public TLAPlusBinaryOperator(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public TLAPlusExpression getOperandOne() {
        return this.operandOne;
    }

    public TLAPlusExpression getOperandTwo() {
        return this.operandTwo;
    }

    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
