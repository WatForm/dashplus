package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.operandOne);
        children.add(this.operandTwo);
        return children;
    }

}
