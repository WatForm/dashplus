package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusUnaryOperator extends TLAPlusOperator {

    private TLAPlusExpression operand;

    public TLAPlusUnaryOperator(
            TLAPlusExpression operand, TLAPlusOperator.PrecedenceGroup precedenceGroup) {

        super(TLAPlusOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.operand = operand;
    }

    public TLAPlusExpression getOperand() {
        return this.operand;
    }

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.operand);
        return children;
    }
}
