package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusBinaryOperator extends TLAPlusOperator {

    public final TLAPlusExpression operandOne;
    public final TLAPlusExpression operandTwo;

    public TLAPlusBinaryOperator(
            TLAPlusExpression operandOne,
            TLAPlusExpression operandTwo,
            TLAPlusOperator.Associativity associativity,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {
        super(associativity, precedenceGroup);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(this.operandOne, this.operandTwo);
    }
}
