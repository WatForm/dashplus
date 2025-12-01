package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusQuantificationOperator extends TLAPlusOp {

    public final TLAPlusVar variable; // bound variable
    public final TLAPlusExp set; // set that the iteration takes place over
    public final TLAPlusExp expression; // expression used

    public TLAPlusQuantificationOperator(
            TLAPlusVar variable,
            TLAPlusExp set,
            TLAPlusExp expression,
            TLAPlusOp.PrecedenceGroup precedenceGroup) {
        super(TLAPlusOp.Associativity.IRRELEVANT, precedenceGroup);
        this.variable = variable;
        this.expression = expression;
        this.set = set;
    }

    public List<TLAPlusExp> getChildren() {
        return Arrays.asList(this.variable, this.set, this.expression);
    }

    // this is a common style for exists and for-all
    static String predicateSnippetCore(TLAPlusQuantificationOperator o, String symbol) {
        return symbol
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.variable)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.set)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.COLON
                + TLAPlusStrings.SPACE
                + o.getTLASnippetOfChild(o.expression);
    }
}
