package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusQuantificationOperator extends TLAPlusOperator {

    private final TLAPlusVariable variable; // bound variable
    private final TLAPlusExpression set; // set that the iteration takes place over
    private final TLAPlusExpression expression; // expression used

    public TLAPlusQuantificationOperator(
            TLAPlusVariable variable,
            TLAPlusExpression set,
            TLAPlusExpression expression,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.variable = variable;
        this.expression = expression;
        this.set = set;
    }

    public TLAPlusExpression getSet() {
        return this.set;
    }

    public TLAPlusVariable getVariable() {
        return this.variable;
    }

    public TLAPlusExpression getExpression() {
        return this.expression;
    }

    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(this.variable, this.set, this.expression);
    }

    // this is a common style for exists and for-all
    static String predicateSnippetCore(TLAPlusQuantificationOperator o, String symbol) {
        return symbol
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.getVariable())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.getSet())
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.getExpression());
    }
}
