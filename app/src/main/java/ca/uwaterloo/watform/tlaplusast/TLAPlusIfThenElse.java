package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TLAPlusIfThenElse extends TLAPlusOperator {

    public final TLAPlusExpression condition;
    public final TLAPlusExpression thenExpression;
    public final TLAPlusExpression elseExpression;

    public TLAPlusIfThenElse(
            TLAPlusExpression condition,
            TLAPlusExpression thenExpression,
            TLAPlusExpression elseExpression) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, TLAPlusOperator.PrecedenceGroup.SAFE);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(condition, thenExpression, elseExpression);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.IF
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(condition)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.THEN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(thenExpression)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.ELSE
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(elseExpression);
    }
}
