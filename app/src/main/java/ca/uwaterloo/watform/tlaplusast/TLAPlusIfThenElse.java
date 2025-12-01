package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TLAPlusIfThenElse extends TLAPlusOp {

    public final TLAPlusExp condition;
    public final TLAPlusExp thenExpression;
    public final TLAPlusExp elseExpression;

    public TLAPlusIfThenElse(
            TLAPlusExp condition,
            TLAPlusExp thenExpression,
            TLAPlusExp elseExpression) {
        super(TLAPlusOp.Associativity.IRRELEVANT, TLAPlusOp.PrecedenceGroup.SAFE);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public List<TLAPlusExp> getChildren() {
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
