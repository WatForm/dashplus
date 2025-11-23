package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusBinaryOperator extends TLAPlusOperator {

    private final TLAPlusExpression operandOne;
    private final TLAPlusExpression operandTwo;

    public TLAPlusBinaryOperator(
            TLAPlusExpression operandOne,
            TLAPlusExpression operandTwo,
            TLAPlusOperator.Associativity associativity,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {
        super(associativity, precedenceGroup);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public TLAPlusExpression getOperandOne() {
        return this.operandOne;
    }

    public TLAPlusExpression getOperandTwo() {
        return this.operandTwo;
    }

    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(this.operandOne, this.operandTwo);
    }
}
