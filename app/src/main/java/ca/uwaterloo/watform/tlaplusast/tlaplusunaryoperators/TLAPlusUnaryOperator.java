package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusUnaryOperator extends TLAPlusOperator {

    public final TLAPlusExpression operand;

    public TLAPlusUnaryOperator(
            TLAPlusExpression operand, TLAPlusOperator.PrecedenceGroup precedenceGroup) {

        super(TLAPlusOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.operand = operand;
    }

    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(this.operand);
    }
}
