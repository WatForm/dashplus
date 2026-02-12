package ca.uwaterloo.watform.tlaast;

import java.util.Arrays;
import java.util.List;

public class TlaIfThenElse extends TlaOperator {

    /*
    IF P THEN e1 ELSE e2
    */

    public final TlaExp condition; // P
    public final TlaExp thenExpression; // e1
    public final TlaExp elseExpression; // e2

    public TlaIfThenElse(TlaExp condition, TlaExp thenExpression, TlaExp elseExpression) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.UNSAFE);
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
