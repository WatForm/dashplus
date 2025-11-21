package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public abstract class TLAPlusInfixBinayOperator extends TLAPlusBinaryOperator {

    private String infixOperator;

    public TLAPlusInfixBinayOperator(
            String infixOperator, TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {

        super(operandOne, operandTwo);
        this.infixOperator = infixOperator;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        this.getOperandOne().toString(sb, ident);
        sb.append(TLAPlusStrings.SPACE + this.infixOperator + TLAPlusStrings.SPACE);
        this.getOperandTwo().toString(sb, ident);

        return;
    }
}
