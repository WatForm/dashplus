package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.utils.*;

public abstract class TLAPlusBinOperatorInfix extends TLAPlusBinaryOperator {

    public TLAPlusBinOperatorInfix(
            String infixOperator, TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {

        super(operandOne, operandTwo);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getOperandOne().toString());
        // TODO fix this
        return result.toString();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
