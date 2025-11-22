package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public abstract class TLAPlusInfixBinaryOperator extends TLAPlusBinaryOperator {

    private final String infixOperator;

    public TLAPlusInfixBinaryOperator(
            String infixOperator,
            TLAPlusExpression operandOne,
            TLAPlusExpression operandTwo,
            TLAPlusOperator.Associativity associativity,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {

        super(operandOne, operandTwo, associativity, precedenceGroup);
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
