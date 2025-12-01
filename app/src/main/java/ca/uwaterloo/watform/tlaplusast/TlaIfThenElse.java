package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TlaIfThenElse extends TlaOperator {

    public final TlaExp condition;
    public final TlaExp thenExpression;
    public final TlaExp elseExpression;

    public TlaIfThenElse(
            TlaExp condition,
            TlaExp thenExpression,
            TlaExp elseExpression) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public List<TlaExp> getChildren() {
        return Arrays.asList(condition, thenExpression, elseExpression);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.IF
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(condition)
                + TlaStrings.SPACE
                + TlaStrings.THEN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(thenExpression)
                + TlaStrings.SPACE
                + TlaStrings.ELSE
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(elseExpression);
    }
}
